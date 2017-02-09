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
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ItemsDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(ItemsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    @Autowired
    EventBus eventBus;

    @Autowired
    ParametersDao parametersDao;

    @Autowired
    AnnotationsDao annotationsDao;

    @Autowired
    CounterDao counterDao;

    @Autowired
    PartsDao partsDao;

    public Boolean initialize(String domainUri){
        String query = "create table if not exists items(uri varchar, time text, tokens text, primary key(uri));";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Initialized tokens table for '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    public boolean exists(String domainId, String itemId){

        String itemUri = URIGenerator.fromId(Resource.Type.ITEM, itemId);

        String query = "select count(*) from items where "+
                "uri='"+ itemUri + "' "+
                "ALLOW FILTERING;";
        try{
            ResultSet result = dbSessionManager.getSessionById(domainId).execute(query);
            return result.one().getLong(0) == 1;
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }


    }

    public String getField(String itemUri, String field) throws DataNotFound {

        String query = "select "+field+ " from items where "+
                "uri='"+ itemUri+ "';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);

            Row row = result.one();

            if (row == null || Strings.isNullOrEmpty(row.getString(0))) throw new DataNotFound("Item not found by uri" +
                    " " +
                    "'"+itemUri+"'");
            return row.getString(0);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            throw new DataNotFound("Error getting Item by uri '"+itemUri+"'");
        }

    }


    public Boolean remove(String domainUri){
        String query = "drop table if exists items;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);

            LOG.info("Removed tokens tables");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    public Boolean saveOrUpdateTokens(String domainUri, String uri, String tokens){
        String query = "insert into items (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', '"+ tokens+"');";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("saved tokens of '"+uri+"' to '"+domainUri+"'");


            Domain resource = new Domain();
            resource.setUri(domainUri);
            eventBus.post(Event.from(resource), RoutingKey.of(resource.getResourceType(), Resource.State.UPDATED));

            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }

    }

    public Boolean add(String domainUri, String uri){

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
                Annotation annotation = annotationsDao.get(uri, type);
                tokens += annotation.getValue();
            } catch (DataNotFound dataNotFound) {
                LOG.debug("No '"+type+"' found by '"+uri+"'");
            }
        }


        String query = "insert into items (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', '"+tokens+"');";
        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Added item '"+uri+"' to '"+domainUri+"' by " + tokenizerMode);
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }

    }

    public Boolean remove(String domainUri, String uri){
        String query = "delete from items where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);

            // update counter
            counterDao.decrement(domainUri,Resource.Type.ITEM.route());

            // delete parts of item
            Optional<String> offset = Optional.empty();
            Boolean completed = false;
            while(!completed){

                List<String> parts = listParts(uri, 100, offset);
                for (String partUri : parts){
                    partsDao.remove(domainUri, partUri);
                }

                if (parts.size() < 100) break;

                offset = Optional.of(parts.get(parts.size()-1));
            }

            LOG.info("Removed tokens of '"+uri+"' from '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }

    }

    public String getTokens(String domainUri, String uri) throws DataNotFound {
        String query = "select tokens from items where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            Row row = result.one();

            if (row == null) throw new DataNotFound("Tokens from '"+uri+"' not found in '"+domainUri+"'");

            return row.getString(0);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            throw new DataNotFound("Error getting Tokens from '"+uri+"' in '"+domainUri+"'");
        }

    }

    public List<String> listParts(String itemUri, Integer size, Optional<String> offset){
        StringBuilder query = new StringBuilder().append("select starturi from describes where enduri='"+ itemUri+"'");

        if (offset.isPresent()){
            query.append(" where token(starturi) >= token('" + URIGenerator.fromId(Resource.Type.PART,offset.get()) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query.toString());

            List<Row> rows = result.all();

            if ((rows == null) || (rows.isEmpty())) Collections.emptyList();

            return rows.stream().map(row -> row.getString(0)).collect(Collectors.toList());
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> listAt(String domainUri, Integer size, Optional<String> offset) throws DataNotFound {
        StringBuilder query = new StringBuilder().append("select uri from items");

        if (offset.isPresent()){
            query.append(" where token(uri) >= token('" + URIGenerator.fromId(Resource.Type.ITEM,offset.get()) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query.toString());

            List<Row> rows = result.all();

            if ((rows == null) || (rows.isEmpty())) Collections.emptyList();

            return rows.stream().map(row -> row.getString(0)).collect(Collectors.toList());
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> listAll(Integer size, Optional<String> offset) {
        StringBuilder query = new StringBuilder().append("select uri from items");

        if (offset.isPresent()){
            query.append(" where token(uri) >= token('" + URIGenerator.fromId(Resource.Type.ITEM,offset.get()) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query.toString());

            List<Row> rows = result.all();

            if ((rows == null) || (rows.isEmpty())) return Collections.emptyList();

            return rows.stream().map(row -> row.getString(0)).collect(Collectors.toList());
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return Collections.emptyList();
        }
    }


}
