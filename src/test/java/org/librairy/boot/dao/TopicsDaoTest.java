/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.domain.resources.Shape;
import org.librairy.boot.model.domain.resources.TopicDescription;
import org.librairy.boot.model.domain.resources.WordDescription;
import org.librairy.boot.storage.dao.TopicsDao;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoTest.class)
public class TopicsDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(TopicsDaoTest.class);

    @Autowired
    TopicsDao dao;


    @Test
    public void list(){

        List<TopicDescription> topics = dao.get("kcap", Optional.empty());

        for(TopicDescription topic : topics){
            LOG.info("Topic: " + topic);
        }

    }

    @Test
    public void shape(){

        List<Double> shape = dao.getShapeOf("http://librairy.linkeddata.es/resources/items/88d6cc47612030117452ad52f22f3017", "kcap");

        LOG.info(shape.stream().map(score -> String.valueOf(score)).collect(Collectors.joining(" | ")));

    }

    @Test
    public void save(){

        String domainId     = "test1";
        Integer numTopics   = 5;
        Integer numWords    = 10;
        Integer numDocs     = 10;

        List<TopicDescription> topics = new ArrayList<>();
        for(int i=0;i<numTopics;i++){
            TopicDescription topic = new TopicDescription();
            topic.setId(String.valueOf(i));
            topic.setDomainId(domainId);
            for(int j=0;j<numWords;j++){
                WordDescription wordDescription = new WordDescription();
                wordDescription.setValue("word"+j);
                wordDescription.setScore(Double.valueOf(System.currentTimeMillis())/Double.MAX_VALUE);
                topic.add(wordDescription);
            }
            topics.add(topic);
        }

        List<Shape> shapes = new ArrayList<>();
        for(int i=0;i<numDocs;i++){
            Shape shape = new Shape();
            shape.setType("item");
            shape.setUri(URIGenerator.fromId(Resource.Type.ITEM,"test"+i));
            shape.setVector(IntStream.range(0, numTopics).mapToDouble(ind -> 0.2).boxed().collect(Collectors.toList()));
            shapes.add(shape);
        }


        dao.save(domainId,topics,shapes);

    }

}
