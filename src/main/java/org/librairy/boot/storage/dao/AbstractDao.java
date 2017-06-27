/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public abstract class AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDao.class);

    protected final Escaper escaper = Escapers.builder()
            .addEscape('\'',"")
            .addEscape('\"',"")
            .addEscape('('," ")
            .addEscape(')'," ")
            .addEscape('['," ")
            .addEscape(']'," ")
            .build();

    @Autowired
    protected DBSessionManager dbSessionManager;


    protected void truncateQuery(String tableName){
        String query = "truncate "+tableName+";";
        try{
            dbSessionManager.getCommonSession().execute(query);
        } catch (InvalidQueryException e){
            LOG.warn(e.getMessage());
        } catch (Exception e){
            LOG.warn("Unexpected error deleting table '" + tableName +"'",e);
        }
    }

//    protected void truncateQueryOn(String tableName, String domainId){
//        String query = "truncate "+tableName+";";
//        try{
//            dbSessionManager.getDomainSession(domainId).execute(query);
//        } catch (InvalidQueryException e){
//            LOG.warn(e.getMessage());
//        } catch (Exception e){
//            LOG.warn("Unexpected error deleting table '" + tableName +"'",e);
//        }
//    }

    protected Boolean countQuery(String query){
        LOG.debug("Executing count query: " + query);
        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            if (result == null) return false;

            Row row = result.one();
            if ((row == null) || row.getLong(0) < 1) return false;

        } catch (InvalidQueryException e){
            LOG.warn("Error on count query execution [" + query + "] : " + e.getMessage());
            return false;
        }
        return true;
    }

//    protected Boolean countQueryOn(String query, String domainId){
//        LOG.debug("Executing count query: " + query  +" on domain: " + domainId);
//        try{
//            ResultSet result = dbSessionManager.getDomainSession(domainId).execute(query);
//            if (result == null) return false;
//
//            Row row = result.one();
//            if ((row == null) || row.getLong(0) < 1) return false;
//
//        } catch (InvalidQueryException e){
//            LOG.warn("Error on count query execution [" + query + "] : " + e.getMessage());
//            return false;
//        }
//        return true;
//    }

    protected Iterator<Row> iteratedQuery(String query){

        LOG.debug("Executing iterated query: " + query);
        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query.toString());
            if (result == null) return Collections.emptyIterator();
            return result.iterator();

        }catch (InvalidQueryException e){
            LOG.warn("Error on iterated query execution [" + query + "] : " + e.getMessage());
            return Collections.emptyIterator();
        }
    }

//    protected Iterator<Row> iteratedQueryOn(String query, String domainId){
//
//        LOG.debug("Executing iterated query: " + query + " on domain: " + domainId);
//        try{
//            ResultSet result = dbSessionManager.getDomainSession(domainId).execute(query.toString());
//            if (result == null) return Collections.emptyIterator();
//            return result.iterator();
//
//        }catch (InvalidQueryException e){
//            LOG.warn("Error on iterated query execution [" + query + "] on domain '"+domainId+"': " + e.getMessage());
//            return Collections.emptyIterator();
//        }
//    }

    protected Optional<Row> oneQuery(String query){

        LOG.debug("Executing one query: " + query);
        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query.toString());
            if (result == null) return Optional.empty();
            Row value = result.one();
            if (value == null) return Optional.empty();
            return Optional.of(value);

        }catch (InvalidQueryException e){
            LOG.warn("Error on one query execution [" + query + "] : " + e.getMessage());
            return Optional.empty();
        }
    }

//    protected Optional<Row> oneQueryOn(String query, String domainId){
//
//        LOG.debug("Executing one query: " + query + " on domain: " + domainId);
//        try{
//            ResultSet result = dbSessionManager.getDomainSession(domainId).execute(query.toString());
//            if (result == null) return Optional.empty();
//            Row value = result.one();
//            if (value == null) return Optional.empty();
//            return Optional.of(value);
//
//        }catch (InvalidQueryException e){
//            LOG.warn("Error on one query execution [" + query + "] on domain '"+domainId+"': " + e.getMessage());
//            return Optional.empty();
//        }
//    }


    protected Boolean execute(String query){

        LOG.debug("Executing query: " + query );
        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query.toString());
            if (result == null) return false;
            return result.wasApplied();

        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "]: " + e.getMessage());
            return false;
        }
    }

//    protected Boolean executeOn(String query, String domainId){
//
//        LOG.debug("Executing query: " + query + " on domain: " + domainId);
//        try{
//            ResultSet result = dbSessionManager.getDomainSession(domainId).execute(query.toString());
//            if (result == null) return false;
//            return result.wasApplied();
//
//        }catch (InvalidQueryException e){
//            LOG.warn("Error on query execution [" + query + "] on domain '"+domainId+"': " + e.getMessage());
//            return false;
//        }
//    }

}
