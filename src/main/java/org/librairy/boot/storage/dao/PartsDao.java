/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Part;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class PartsDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(PartsDao.class);

    @Autowired
    UDM udm;

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    CounterDao counterDao;

    @Autowired
    DomainsDao domainsDao;

    @Autowired
    AnnotationsDao annotationsDao;

    public boolean save(Part part){
        try{
            udm.save(part);
        }catch (Exception e){
            LOG.error("Unexpected error saving part: " + part, e);
            return false;
        }
        return true;
    }

    public boolean delete(String partUri){
        try{
            // delete from research.describes

            Iterator<Row> rows = super.iteratedQuery("select uri from describes where starturi='" + partUri + "';");

            if (rows != null){
                while(rows.hasNext()){
                    String uri = rows.next().getString(0);
                    super.execute("delete from describes where uri='"+uri+"';");

                    // descrease counter (n-times)s
                    counterDao.decrement(Relation.Type.DESCRIBES.route());
                }
            }

            // delete annotations
            annotationsDao.removeByResource(partUri, Optional.empty());

            // Delete
            udm.delete(Resource.Type.PART).byUri(partUri);
        }catch (Exception e){
            LOG.error("Unexpected error deleting part: " + partUri, e);
            return false;
        }
        return true;
    }

    public void deleteAll(){
        LOG.info("Deleting all parts from database..");

        truncateQuery("parts");
        counterDao.reset(Resource.Type.PART.route());
        truncateQuery("describes");
        counterDao.reset(Relation.Type.DESCRIBES.route());

        Integer windowSize = 100;
        Optional<String> offset = Optional.empty();
        Boolean finished = false;

        while(!finished){
            List<Domain> d = domainsDao.list(windowSize, offset, false);

            for (Domain domain: d){
                domainsDao.removeAllParts(domain.getUri());
            }

            if (d.size() < windowSize) break;

            offset = Optional.of(URIGenerator.retrieveId(d.get(windowSize-1).getUri()));

        }

        LOG.info("All parts deleted!");
    }


    public Boolean exists(String partUri){
        return super.countQuery("select count(uri) from parts where uri='" + partUri + "';");
    }

    public Optional<Part> get(String uri, Boolean content) {
        StringBuilder query = new StringBuilder().append("select creationtime, sense ");

        if (content){
            query.append(", content ");
        }

        query.append("from parts where uri='"+uri+"';");

        Optional<Row> row = super.oneQuery(query.toString());

        if (!row.isPresent()) return Optional.empty();

        Row rowValue = row.get();
        Part part = new Part();
        part.setUri(uri);
        part.setCreationTime(rowValue.getString(0));
        part.setSense(rowValue.getString(1));

        if (content){
            part.setContent(rowValue.getString(2));
        }

        return Optional.of(part);
    }


    public List<Part> list(Integer size, Optional<String> offset, Boolean inclusive){

        StringBuilder query = new StringBuilder().append("select uri, sense, creationtime from parts");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" and token(uri) "+operator+" token('" + offset.get() + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        Iterator<Row> it = super.iteratedQuery(query.toString());
        List<Part> parts = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Part part = new Part();
            part.setUri(row.getString(0));
            part.setSense(row.getString(1));
            part.setCreationTime(row.getString(2));
            parts.add(part);
        }

        return parts;

    }


    public List<Item> listItems(String partUri, Integer size, Optional<String> offset, Boolean inclusive){

        StringBuilder query = new StringBuilder().append("select enduri from describes where starturi='"+ partUri+"'");

        if (offset.isPresent()){

            ResultSet offsetRes = dbSessionManager.getCommonSession().execute("select uri from describes where enduri='" + URIGenerator.fromId(Resource.Type.ITEM, offset.get()) + "' and starturi='"+partUri+"' ALLOW FILTERING;");

            Row offsetRow = offsetRes.one();

            if (offsetRow == null) return Collections.emptyList();

            String operator = inclusive? ">=" : ">";
            query.append(" and token(uri) "+operator+" token('" + offsetRow.getString(0) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        Iterator<Row> it = super.iteratedQuery(query.toString());
        List<Item> items = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Item item = new Item();
            item.setUri(row.getString(0));
            items.add(item);
        }

        return items;

    }

    public List<Domain> listDomains(String partUri, Integer size, Optional<String> offset){

        Integer windowSize = 100;
        Optional<String> offsetItem = Optional.empty();
        Boolean finished = false;
        List<Domain> domains = new ArrayList<>();
        while(!finished){

            List<Item> items = listItems(partUri, windowSize, offsetItem, false);

            for (Item item: items){

                Optional<String> offsetDomain = Optional.empty();
                Boolean domainFinished = false;
                while(!domainFinished){
                    List<Domain> domainsPerItem = itemsDao.listDomains(item.getUri(), windowSize, offsetDomain, false);

                    for(Domain domain: domainsPerItem){

                        if (!domains.contains(domain)){
                            domains.add(domain);
                        }

                    }

                    if (domainsPerItem.size() < windowSize) break;

                    offsetDomain = Optional.of(URIGenerator.retrieveId(domainsPerItem.get(windowSize-1).getUri()));

                }


            }

            if ((items.size() < windowSize) || (domains.size() >= size)) break;

            offsetItem = Optional.of(URIGenerator.retrieveId(items.get(windowSize-1).getUri()));
        }
        return domains;
    }


}
