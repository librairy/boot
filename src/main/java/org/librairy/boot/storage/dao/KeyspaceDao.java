/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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
    DBSessionManager dbSessionManager;


//    public Boolean createKeyspace(String uri){
//        String id = DBSessionManager.getKeyspaceFromUri(uri);
//        String query = "create keyspace if not exists "+id+" with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1} and DURABLE_WRITES = false;";
////        String query = "create keyspace if not exists "+id+" with replication = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 3 } and DURABLE_WRITES = false;";
//        LOG.debug("Executing query: " + query);
//        ResultSet result = dbSessionManager.getSession().execute(query);
//        return result.wasApplied();
//    }
//
//    public Boolean removeKeyspace(String uri){
//        String id = DBSessionManager.getKeyspaceFromUri(uri);
//        String query = "drop keyspace if exists "+id+";";
//        LOG.debug("Executing query: " + query);
//        ResultSet result = dbSessionManager.getSession().execute(query);
//        return result.wasApplied();
//    }

}
