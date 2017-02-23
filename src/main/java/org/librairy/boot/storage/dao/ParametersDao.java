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
import org.librairy.boot.storage.actions.DeleteResourceAction;
import org.librairy.boot.storage.exception.DataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ParametersDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(ParametersDao.class);

    @Autowired
    DBSessionManager dbSessionManager;


    public Boolean initialize(String domainUri){
        String query = "create table if not exists parameters(name varchar, value varchar, primary key(name));";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            //TODO this part should be moved to specific modules
            saveOrUpdate(domainUri,"tokenizer.mode","lemma");
            saveOrUpdate(domainUri,"lda.delay","360000");
            saveOrUpdate(domainUri,"lda.vocabulary.size","10000");
            saveOrUpdate(domainUri,"lda.optimizer","basic");
            saveOrUpdate(domainUri,"lda.max.evaluations","50");
            saveOrUpdate(domainUri,"lda.max.iterations","10");
            saveOrUpdate(domainUri,"lda.features","TF");
            saveOrUpdate(domainUri,"lda.topics","-1");
            saveOrUpdate(domainUri,"lda.alpha","-1");
            saveOrUpdate(domainUri,"lda.beta","-1");
            saveOrUpdate(domainUri,"w2v.delay","300000");
            saveOrUpdate(domainUri,"w2v.dim","100");
            saveOrUpdate(domainUri,"w2v.max.iterations","20");

            LOG.info("Initialized internal parameters tables for '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }

    }

    public Boolean remove(String domainUri){
        String query = "drop table if exists parameters;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Removed domains internal tables");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }
    }

    public Boolean saveOrUpdate(String domainUri, String name, String value){
        String query = "insert into parameters (name,value) values('"+name+"', '"+ value+"');";

        ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);

        LOG.info("Added name: '"+name+"' = '"+value+"' in '"+domainUri+"'");
        return result.wasApplied();
    }

    public Boolean remove(String domainUri, String name){
        String query = "delete from parameters where name='"+name+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Removed name: '"+name+"' from '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return false;
        }
    }

    public String get(String domainUri, String name) throws DataNotFound {
        String query = "select value from parameters where name='"+name+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            Row row = result.one();

            if (row == null) throw new DataNotFound("Parameter '"+name+"' not found in '"+domainUri+"'");

            return row.getString(0);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            throw new DataNotFound("No Parameter '"+name+"' found in '"+domainUri+"'");
        }
    }



}
