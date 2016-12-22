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
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ItemsDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(ItemsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;


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

        String query = "select "+field+ "from items where "+
                "uri='"+ itemUri+ "';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if (row == null) throw new DataNotFound("Item not found by uri '"+itemUri+"'");

            return result.one().getString(0);
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
        String query = "insert into items (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', \""+
                tokens+"\");";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("saved tokens of '"+uri+"' to '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }

    }

    public Boolean add(String domainUri, String uri){
        String query = "insert into items (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', '');";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Added item '"+uri+"' to '"+domainUri+"'");
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
            LOG.info("Removed tokens of '"+uri+"' from '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }

    }

    public String get(String domainUri, String uri) throws DataNotFound {
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



}
