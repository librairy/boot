/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.domain.resources.Shape;
import org.librairy.boot.model.domain.resources.TopicDescription;
import org.librairy.boot.model.domain.resources.WordDescription;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class TopicsDao extends AbstractDao {

    @Autowired
    CounterDao counterDao;

    @Autowired
    KeyspaceDao keyspaceDao;

    private static final Logger LOG = LoggerFactory.getLogger(TopicsDao.class);

    public List<TopicDescription> get(String domainId, Optional<Integer> numWords){
        try{
            // delete from research.describes
            String query = "select id, elements, scores from topics ;";
            String scope = "lda";

            Iterator<Row> rows = super.iteratedQueryOn(query,scope,domainId);
            List<TopicDescription> topics = new ArrayList<>();
            if (rows != null){
                while(rows.hasNext()){

                    Row row = rows.next();
                    Long topicId = row.getLong(0);
                    List<String> words = row.getList(1, String.class);
                    List<Double> scores = row.getList(2, Double.class);

                    TopicDescription topicDescription = new TopicDescription();
                    topicDescription.setDomainId(domainId);
                    topicDescription.setId(String.valueOf(topicId));

                    int wordsLimit = numWords.isPresent()? numWords.get() : 20;

                    for(int i=0;i<wordsLimit;i++){
                        WordDescription wordDescription = new WordDescription();
                        wordDescription.setValue(words.get(i));
                        wordDescription.setScore(scores.get(i));
                        topicDescription.add(wordDescription);
                    }

                    topics.add(topicDescription);
                }
            }
            return topics.stream().sorted((a,b) -> Integer.valueOf(a.getId()).compareTo(Integer.valueOf(b.getId()))).collect(Collectors.toList());

        }catch (Exception e){
            LOG.error("Unexpected error getting topics of " + domainId, e);
            return Collections.emptyList();
        }
    }

    public List<Double> getShapeOf(String uri, String domainId){
        try{
            String query = "select vector from shapes where uri='"+uri+"' ;";
            String scope = "lda";

            Optional<Row> row = super.oneQueryOn(query, scope, domainId);

            if (row.isPresent()){
                return row.get().getList(0,Double.class);
            }

            LOG.warn("Not found resource: " + uri +" in domain: " + domainId);
            return Collections.emptyList();

        }catch (Exception e){
            LOG.error("Unexpected error getting shape of " + uri + " in " + domainId, e);
            return Collections.emptyList();
        }
    }

    public boolean save(String domainId, List<TopicDescription> topics, List<Shape> shapes){

        try {
            initialize(domainId);

            Session session = super.dbSessionManager.getSpecificSession("lda", domainId);

            // save topics
            LOG.debug("saving topics..");
            PreparedStatement topicStatement = session.prepare("insert into topics (uri, id, date, description, elements, scores) values (?, ?, ?, ?, ?, ?)");

            AtomicInteger counter = new AtomicInteger();
            for(TopicDescription topicDescription : topics){
                if (counter.incrementAndGet()%100 == 0) Thread.sleep(100);
                String uri = URIGenerator.compositeFromContent(Resource.Type.DOMAIN, domainId, Resource.Type.TOPIC, topicDescription.getId());
                session.executeAsync(topicStatement.bind(
                        uri,
                        Long.valueOf(topicDescription.getId()),
                        TimeUtils.asISO(),
                        "created from external resource",
                        topicDescription.getWords().stream().map(wd -> wd.getValue()).collect(Collectors.toList()),
                        topicDescription.getWords().stream().map(wd -> wd.getScore()).collect(Collectors.toList())));
            }
            LOG.debug("topics saved");


            // save shapes
            LOG.debug("saving shapes..");
            PreparedStatement resourceStatement = session.prepare("insert into distributions (topic_uri, score, resource_uri, date, resource_type) values (?, ?, ?, ?, ?)");
            PreparedStatement shapeStatement    = session.prepare("insert into shapes (id, date, type, uri, vector) values (?, ?, ?, ?, ?)");
            Long index = 0l;
            for(Shape shape: shapes){
                if (counter.incrementAndGet()%100 == 0) Thread.sleep(100);
                session.executeAsync(shapeStatement.bind(
                        index++,
                        TimeUtils.asISO(),
                        shape.getType(),
                        shape.getUri(),
                        shape.getVector()));

                // save resources by topic
                Integer topicIndex = 0;
                for(Double score: shape.getVector()){
                    if (counter.incrementAndGet()%100 == 0) Thread.sleep(100);
                    String topicUri = URIGenerator.compositeFromContent(Resource.Type.DOMAIN, domainId, Resource.Type.TOPIC, String.valueOf(topicIndex++));
                    session.executeAsync(resourceStatement.bind(
                            topicUri,
                            score,
                            shape.getUri(),
                            TimeUtils.asISO(),
                            shape.getType()));
                }
            }
            LOG.debug("shapes saved");
            return true;
        } catch (InterruptedException e) {
            LOG.error("Interrupted",e);
            return false;
        }

    }


    private void initialize(String domainId){
        String domainUri = URIGenerator.fromId(Resource.Type.DOMAIN, domainId);
        counterDao.reset(domainUri, Resource.Type.TOPIC.route());
        counterDao.reset(domainUri, Relation.Type.SIMILAR_TO_ITEMS.route());

        keyspaceDao.destroy(domainId,"lda");
        keyspaceDao.initialize(domainId,"lda");

        // Clean previous model

        String shapesTable = "create table if not exists shapes (id bigint, uri text, type text, vector list<double>, date text, primary key(id));";
        super.executeOn(shapesTable,"lda",domainId);
        super.executeOn("create index if not exists on shapes (uri);","lda",domainId);
        super.executeOn("create index if not exists on shapes (type);","lda",domainId);

        String distributionsTable = "create table if not exists distributions (topic_uri text, score double, resource_uri text, date text, resource_type text, primary key(topic_uri, score, resource_uri)) with clustering order by (score DESC, resource_uri ASC);";
        super.executeOn(distributionsTable,"lda",domainId);
        super.executeOn("create index if not exists on distributions (resource_type);","lda",domainId);

        String topicsTable = "create table if not exists topics (uri text, id bigint, date text, description text, elements list<text>, scores list<double>, primary key(uri,id)) ;";
        super.executeOn(topicsTable,"lda",domainId);
    }
}
