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
import com.google.common.base.Strings;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.*;
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

import java.util.*;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class DomainsDao extends AbstractDao  {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(DomainsDao.class);

    @Autowired
    KeyspaceDao keyspaceDao;

    @Autowired
    UDM udm;

    @Autowired
    EventBus eventBus;

    @Autowired
    CounterDao counterDao;

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    ParametersDao parametersDao;

    @Autowired
    AnnotationsDao annotationsDao;

    public Boolean exists(String domainUri){
        return super.countQuery("select count(uri) from domains where uri='" + domainUri + "';");
    }


    public Boolean contains(String domainUri, String resourceUri){
        String domainId = URIGenerator.retrieveId(domainUri);
        String tableName    = URIGenerator.typeFrom(resourceUri).route();
        return super.countQueryOn("select count(uri) from "+tableName+" where uri='" + resourceUri+ "' ;", domainId);
    }


    public Domain get(String domainUri) throws DataNotFound {

        Optional<Row> row = super.oneQuery("select name, description, creationtime from domains where uri='"+domainUri+"';");

        if (!row.isPresent()) throw new DataNotFound("No domain found by '" + domainUri + "'");

        Domain domain = new Domain();
        Row rowValue = row.get();
        domain.setName(rowValue.getString(0));
        domain.setDescription(rowValue.getString(1));
        domain.setCreationTime(rowValue.getString(2));
        domain.setUri(domainUri);
        return domain;

    }

    public List<Domain> list(Integer size, Optional<String> offset, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select uri, name, description, creationtime from domains");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" where token(uri) "+operator+" token('" + URIGenerator.fromId(Resource.Type.DOMAIN, offset.get())+"') ");
        }

        query.append(" limit " + size);
        query.append(";");

        List<Domain> domains = new ArrayList<>();
        Iterator<Row> it = super.iteratedQuery(query.toString());
        while(it.hasNext()){
            Row row = it.next();
            Domain domain = new Domain();
            domain.setUri(row.getString(0));
            domain.setName(row.getString(1));
            domain.setDescription(row.getString(2));
            domain.setCreationTime(row.getString(3));
            domains.add(domain);
        }

        return domains;

    }

    public List<Item> listDocuments(String domainUri, Integer size, Optional<String> offset, Boolean inclusive) throws DataNotFound {

        Iterator<Row> it = listResources(domainUri, size, offset, Resource.Type.ITEM, inclusive);

        List<Item> items = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Item item = new Item();
            item.setUri(row.getString(0));
            item.setCreationTime(row.getString(1));
            items.add(item);
        }

        return items;
    }

    public List<Part> listParts(String domainUri, Integer size, Optional<String> offset, Boolean inclusive) throws DataNotFound {

        Iterator<Row> it = listResources(domainUri, size, offset, Resource.Type.PART, inclusive);

        List<Part> parts = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Part part = new Part();
            part.setUri(row.getString(0));
            part.setCreationTime(row.getString(1));
            parts.add(part);
        }

        return parts;
    }

    private Iterator<Row> listResources(String domainUri, Integer size, Optional<String> offset, Resource.Type type, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select uri, time from " + type.route() + " ");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" where token(uri) "+operator+" token('" + URIGenerator.fromId(type, offset.get()) + "')");
        }

        query.append(" limit " + size);
        query.append(";");

        String domainId = URIGenerator.retrieveId(domainUri);
        return super.iteratedQueryOn(query.toString(), domainId );
    }


    public boolean save(Domain domain){
        try{
            udm.save(domain);
            return true;
        }catch (Exception e){
            LOG.error("Error saving domain: " + domain);
            return false;
        }
    }


    public void addDocument(String domainUri, String documentUri){

        // add to contains document
        udm.save(Relation.newContains(domainUri, documentUri));

        // add to items document
        StringBuilder insertItemQuery = new StringBuilder()
                .append("insert into items (uri, time, tokens) values(")
                .append("'").append(documentUri).append("',")
                .append("'").append(TimeUtils.asISO()).append("',")
                .append("'").append(retrieveDomainTokens(domainUri, documentUri)).append("'")
                .append(") ;");

        super.executeOn(insertItemQuery.toString(), URIGenerator.retrieveId(domainUri));

        // for each part
        Integer windowSize = 100;
        Optional<String> offset = Optional.empty();
        Boolean finished = false;

        while(!finished){

            List<Part> parts = itemsDao.listParts(documentUri, windowSize, offset, false);

            // add to parts part
            for (Part part: parts){
                StringBuilder insertPartQuery = new StringBuilder()
                        .append("insert into parts (uri, time, tokens) values(")
                        .append("'").append(part.getUri()).append("',")
                        .append("'").append(TimeUtils.asISO()).append("',")
                        .append("'").append(retrieveDomainTokens(domainUri, part.getUri())).append("'")
                        .append(") ;");

                super.executeOn(insertPartQuery.toString(), URIGenerator.retrieveId(domainUri));
                counterDao.increment(domainUri, Resource.Type.PART.route());
            }


            if (parts.size() < windowSize){
                finished = true;
            }else{
                offset = Optional.of(URIGenerator.retrieveId(parts.get(windowSize-1).getUri()));
            }
        }
    }

    private String retrieveDomainTokens(String domainUri, String resourceUri){
        //add tokens from domain parameter
        String tokenizerMode;
        try {
            tokenizerMode = parametersDao.get(domainUri, "tokenizer.mode");
        } catch (DataNotFound dataNotFound) {
            tokenizerMode = "lemma";
        }

        StringTokenizer tokenizer = new StringTokenizer(tokenizerMode, "+", false);
        String tokens = "";
        while(tokenizer.hasMoreTokens()){
            String type = tokenizer.nextToken();
            try {

                for (Annotation annotation : annotationsDao.getByResource(resourceUri, Optional.of(type), Optional.empty(), Optional.empty())){
                    tokens += annotation.getValue().get("content");
                    tokens += " ";
                }

            } catch (DataNotFound dataNotFound) {
                LOG.debug("No annotation '" + type+ "' found for " + resourceUri);
            }
        }

        return Strings.isNullOrEmpty(tokens)? tokens : escaper.escape(tokens);
    }

    public String getDomainTokens(String domainUri, String uri) throws DataNotFound {


        try{
            String domainId     = URIGenerator.retrieveId(domainUri);
            String tableName    = URIGenerator.typeFrom(uri).route();
            Optional<Row> row = super.oneQueryOn("select tokens from "+tableName+" where uri='"+uri+"';", domainId);

            if (!row.isPresent()) throw new DataNotFound("Tokens from '"+uri+"' not found in '"+domainUri+"'");

            return row.get().getString(0);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            throw new DataNotFound("Error getting Tokens from '"+uri+"' in '"+domainUri+"'");
        }

    }

    public Boolean updateDomainTokens(String domainUri, String uri){
        return updateDomainTokens(domainUri, uri, retrieveDomainTokens(domainUri, uri));
    }

    public Boolean updateDomainTokens(String domainUri, String uri, String tokens){

        String domainId = URIGenerator.retrieveId(domainUri);
        Resource.Type type = URIGenerator.typeFrom(uri);
        if (super.executeOn("insert into "+type.route()+" (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', '"+ escaper.escape(tokens) +"');", domainId)){
            LOG.info("saved tokens of '"+uri+"' in '"+domainUri+"'");

            Domain resource = new Domain();
            resource.setUri(domainUri);
            eventBus.post(Event.from(resource), RoutingKey.of(resource.getResourceType(), Resource.State.UPDATED));
            return true;
        }
        return false;
    }

    public void delete(String uri){

        try{
            // delete from contains
            ResultSet result = dbSessionManager.getCommonSession().execute("select uri from contains where starturi='" + uri + "';");
            Iterator<Row> iterator = result.iterator();
            if (iterator != null){
                while(iterator.hasNext()){
                    String relUri = iterator.next().getString(0);
                    udm.delete(Relation.Type.CONTAINS_TO_DOCUMENT).byUri(relUri);
                }
            }

            // delete from domains
            udm.delete(Resource.Type.DOMAIN).byUri(uri);

        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
        }

    }

    public void removeDocument(String domainUri, String documentUri){

        Optional<Row> row = super.oneQuery("select uri from contains where starturi='" + domainUri + "' and enduri='" + documentUri + "' ALLOW FILTERING;");

        if (!row.isPresent()) return;

        // Delete parts
        Integer windowSize = 100;
        Optional<String> offset = Optional.empty();
        Boolean completed = false;
        while(!completed){

            List<Part> parts = itemsDao.listParts(documentUri,windowSize,offset, false);

            for (Part part : parts){
                removePart(domainUri, part.getUri());
            }

            if (parts.size() < windowSize) break;

            offset = Optional.of(URIGenerator.retrieveId(parts.get(parts.size()-1).getUri()));
        }

        udm.delete(Relation.Type.CONTAINS_TO_DOCUMENT).byUri(row.get().getString(0));

        // Remove from items
        super.executeOn("delete from items where uri='"+documentUri+"';", URIGenerator.retrieveId(domainUri));
        counterDao.decrement(domainUri, Resource.Type.ITEM.route());


        LOG.info("Deleted "+ documentUri + " from " + domainUri);

    }

    public boolean removePart(String domainUri, String partUri){
        if (super.executeOn("delete from parts where uri='"+partUri+"';", URIGenerator.retrieveId(domainUri))){
            counterDao.decrement(domainUri, Resource.Type.PART.route());
            return true;
        }
        return false;
    }

    public void removeAllDocuments(String domainUri){

        // Remove from Contains
        Iterator<Row> it = super.iteratedQuery("select uri from contains where starturi='" + domainUri + "';");

        while(it.hasNext()){
            udm.delete(Relation.Type.CONTAINS_TO_DOCUMENT).byUri(it.next().getString(0));
        }
        String domainId = URIGenerator.retrieveId(domainUri);

        // Remove Items
        super.executeOn("drop table if exists items;", domainId);
        counterDao.reset(domainUri, Resource.Type.ITEM.route());

        // Remove Parts
        removeAllParts(domainUri);

    }

    public void removeAllParts(String domainUri){
        String domainId = URIGenerator.retrieveId(domainUri);
        if (super.executeOn("drop table if exists parts;", domainId)){
            counterDao.reset(domainUri, Resource.Type.PART.route());
        }
    }

}
