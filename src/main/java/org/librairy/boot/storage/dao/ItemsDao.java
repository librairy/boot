/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.librairy.boot.model.Annotation;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Part;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ItemsDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsDao.class);

    @Autowired
    UDM udm;

    @Autowired
    DomainsDao domainsDao;

    @Autowired
    PartsDao partsDao;

    @Autowired
    AnnotationsDao annotationsDao;

    public Boolean initialize(String domainUri){
        String domainId = URIGenerator.retrieveId(domainUri);
        return super.executeOn("create table if not exists items(uri varchar, time text, tokens text, primary key(uri));",domainId);
    }

    public boolean save(Item item){
        try{
            udm.save(item);
        }catch (Exception e){
            LOG.error("Unexpected error saving item: " + item, e);
            return false;
        }
        return true;
    }

    public boolean delete(String itemUri){
        try{
            // Delete from domains
            Integer windowSize = 100;
            Optional<String> offset = Optional.empty();
            Boolean finished = false;

            while(!finished){

                List<Domain> domains = listDomains(itemUri, windowSize, offset, false);

                for (Domain domain: domains){
                    domainsDao.removeDocument(domain.getUri(), itemUri);
                }

                if (domains.size() < windowSize) break;

                offset = Optional.of(URIGenerator.retrieveId(domains.get(windowSize-1).getUri()));
            }

            // delete annotations
            annotationsDao.removeAll(itemUri);

            // delete parts
            offset = Optional.empty();
            finished = false;
            while(!finished){
                List<Part> parts = listParts(itemUri, windowSize, offset, false);

                for(Part part: parts){
                    removePart(itemUri, part.getUri());
                }

                if (parts.size() < windowSize) break;

                offset = Optional.of(URIGenerator.retrieveId(parts.get(windowSize-1).getUri()));
            }

            // Delete item
            udm.delete(Resource.Type.ITEM).byUri(itemUri);
        }catch (Exception e){
            LOG.error("Unexpected error deleting item: " + itemUri, e);
            return false;
        }
        return true;
    }

    public boolean addPart(String itemUri, String partUri){
        try{
            udm.save(Relation.newDescribes(partUri,itemUri));
        }catch (Exception e){
            LOG.error("Unexpected error relating part: " + partUri + " to item: " + itemUri, e);
            return false;
        }
        return true;
    }

    public boolean removePart(String itemUri, String partUri){
        try{

            Optional<Row> row = super.oneQuery("select uri from describes where starturi='" + partUri + "' and enduri='" + itemUri + "' ALLOW FILTERING;");
            if (row.isPresent()){
                String uri = row.get().getString(0);

                // remove from domains
                Integer windowSize = 100;
                Optional<String> offset = Optional.empty();
                Boolean finished = false;

                while(!finished){
                    List<Domain> domains = listDomains(itemUri, windowSize, offset, false);

                    for (Domain domain: domains){
                        domainsDao.removePart(domain.getUri(), partUri);
                    }

                    if (domains.size() < windowSize) break;

                    offset = Optional.of(URIGenerator.retrieveId(domains.get(windowSize-1).getUri()));
                }

                // Delete part
                partsDao.delete(uri);

            }
        }catch (Exception e){
            LOG.error("Unexpected error relating part: " + partUri + " to item: " + itemUri, e);
            return false;
        }
        return true;
    }



    public Item get(String uri, Boolean content) throws DataNotFound {

        StringBuilder query = new StringBuilder().append("select description, creationtime, language, format");

        if (content){
            query.append(", content");
        }

        query.append(" from items");

        query.append(" where uri='").append(uri).append("' ");

        query.append(";");

        Optional<Row> row = super.oneQuery(query.toString());



        if (!row.isPresent()) throw new DataNotFound("Document not found: '"+ uri + "'");

        Row rowValue = row.get();
        Item item = new Item();
        item.setUri(uri);
        item.setDescription(rowValue.getString(0));
        item.setCreationTime(rowValue.getString(1));
        item.setLanguage(rowValue.getString(2));
        item.setFormat(rowValue.getString(3));

        if (content){
            item.setContent(rowValue.getString(4));
        }

        return item;

    }

    public Boolean exists(String uri){
        return super.countQuery("select count(*) from items where uri='" + uri + "';");
    }

    public Boolean contains(String itemUri, String partUri){
        return super.countQuery("select count(*) from describes where starturi='" + partUri + "' and enduri='"+itemUri+"' ALLOW FILTERING;");
    }

    public List<Part> listParts(String itemUri, Integer size, Optional<String> offset, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select starturi from describes where enduri='"+ itemUri+"'");

        if (offset.isPresent()){

            ResultSet offsetRes = dbSessionManager.getCommonSession().execute("select uri from describes where starturi='" + URIGenerator.fromId(Resource.Type.PART, offset.get()) + "' and enduri='"+itemUri+"' ALLOW FILTERING;");

            Row offsetRow = offsetRes.one();

            if (offsetRow == null) return Collections.emptyList();

            String operator = inclusive? ">=" : ">";
            query.append(" and token(uri) "+operator+" token('" + offsetRow.getString(0) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        Iterator<Row> it = super.iteratedQuery(query.toString());
        List<Part> parts = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Part part = new Part();
            part.setUri(row.getString(0));
            parts.add(part);
        }

        return parts;
    }

    public List<Item> list(Integer size, Optional<String> offset, Boolean inclusive) {
        StringBuilder query = new StringBuilder().append("select uri, description, creationtime from items");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" where token(uri) "+operator+" token('" + URIGenerator.fromId(Resource.Type.ITEM,offset.get()) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        Iterator<Row> it = super.iteratedQuery(query.toString());

        List<Item> items = new ArrayList<>();
        while(it.hasNext()){
            Row row = it.next();
            Item item = new Item();
            item.setUri(row.getString(0));
            item.setDescription(row.getString(1));
            item.setCreationTime(row.getString(2));
            items.add(item);
        }

        return items;
    }

    public List<Domain> listDomains(String itemUri, Integer size, Optional<String> offset, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select starturi from contains where enduri='"+ itemUri+"'");

        if (offset.isPresent()){

            ResultSet offsetRes = dbSessionManager.getCommonSession().execute("select uri from contains where starturi='" + URIGenerator.fromId(Resource.Type.DOMAIN, offset.get()) + "';");

            Row offsetRow = offsetRes.one();

            if (offsetRow == null) return Collections.emptyList();

            String operator = inclusive? ">=" : ">";
            query.append(" and token(uri) "+operator+" token('" + offsetRow.getString(0) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        Iterator<Row> it = super.iteratedQuery(query.toString());
        List<Domain> domains = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Domain domain = new Domain();
            domain.setUri(row.getString(0));
            domains.add(domain);
        }

        return domains;
    }


}
