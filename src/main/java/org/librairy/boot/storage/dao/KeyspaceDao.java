/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class KeyspaceDao {

    private static final Logger LOG = LoggerFactory.getLogger(KeyspaceDao.class);

    @Autowired
    DBSessionManager sessionManager;


    public void initialize(String domainId, String scope){
        LOG.info("creating a new workspace for domain: " + domainId);
        try{
            sessionManager.getSession().execute("create keyspace if not exists "+DBSessionManager.getSpecificKeyspaceId(scope, domainId)+
                    " with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1} and DURABLE_WRITES = true;");
        }catch (InvalidQueryException e){
            LOG.warn(e.getMessage());
        }
    }

    public void destroy(String domainId, String scope){
        LOG.info("dropping existing workspace for domain: " + domainId);
        try{
            sessionManager.getSession().execute("drop keyspace if exists " + DBSessionManager.getSpecificKeyspaceId(scope, domainId)+ " ;");
        }catch (InvalidQueryException e){
            LOG.warn(e.getMessage());
        }
    }

}
