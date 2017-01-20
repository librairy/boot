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

import java.util.StringTokenizer;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class PartsDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(PartsDao.class);

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

    public Boolean initialize(String domainUri){
        String query = "create table if not exists parts(uri varchar, time text, tokens text, primary key(uri));";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Initialized tokens table for '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }

    }

    public Boolean remove(String domainUri){
        String query = "drop table if exists parts;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Removed tokens tables");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }
    }

    public Boolean saveOrUpdateTokens(String domainUri, String uri, String tokens){
        String query = "insert into parts (uri,tokens,time) values('"+uri+"', '"+ tokens+"', '"+ TimeUtils.asISO()
                +"' );";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Added tokens of '"+uri+"' to '"+domainUri+"'");

            Domain resource = new Domain();
            resource.setUri(domainUri);
            eventBus.post(Event.from(resource), RoutingKey.of(resource.getResourceType(), Resource.State.UPDATED));

            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
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

        String query = "insert into parts (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', '"+tokens+"');";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Added part '"+uri+"' to '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }
    }

    public Boolean remove(String domainUri, String uri){
        String query = "delete from parts where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Removed tokens of '"+uri+"' from '"+domainUri+"'");

            // update counter
            counterDao.decrement(domainUri, Resource.Type.PART.route());

            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }
    }

    public String get(String domainUri, String uri) throws DataNotFound {
        String query = "select tokens from parts where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            Row row = result.one();

            if (row == null) throw new DataNotFound("Tokens from '"+uri+"' not found in '"+domainUri+"'");

            return row.getString(0);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            throw new DataNotFound("Error getting tokens from '"+uri+"' in '"+domainUri+"'");
        }

    }

    public boolean exists(String domainId, String partId){

        String itemUri = URIGenerator.fromId(Resource.Type.ITEM, partId);

        String query = "select count(*) from parts where "+
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

    public String getField(String partUri, String field) throws DataNotFound {

        String query = "select "+field+ " from parts where "+
                "uri='"+ partUri+ "';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);

            Row row = result.one();

            if (row == null || Strings.isNullOrEmpty(row.getString(0))) throw new DataNotFound("Item not found by uri" +
                    " " +
                    "'"+partUri+"'");
            return row.getString(0);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            throw new DataNotFound("Error getting Item by uri '"+partUri+"'");
        }

    }



}
