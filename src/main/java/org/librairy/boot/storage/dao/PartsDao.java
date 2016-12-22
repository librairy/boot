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
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.exception.DataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class PartsDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(PartsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;


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
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }
    }

    public Boolean add(String domainUri, String uri){
        String query = "insert into parts (uri,time,tokens) values('"+uri+"', '"+ TimeUtils.asISO()+"', '');";

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



}
