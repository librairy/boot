/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public abstract class AbstractCounterDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCounterDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    protected Boolean initialize(String keyspaceId){

        String query = "create table if not exists counts(num counter, name varchar, primary key(name));";

        return executeQuery(query,keyspaceId).wasApplied();
    }

    protected Boolean remove(String keyspaceId){

        String query = "drop table if exists counts;";

        return executeQuery(query,keyspaceId).wasApplied();
    }

    protected Boolean truncate(String keyspaceId){

        String query = "truncate counts;";

        return executeQuery(query,keyspaceId).wasApplied();
    }

    protected Boolean update(String keyspaceId, String counter, String op, Long value){

        String query = "update counts set num = num "+op+" "+value+" where name='"+counter+"';";

        return executeQuery(query,keyspaceId).wasApplied();
    }

    protected Long get(String keyspaceId, String counter){

        String query = "select num from counts where name='"+counter+"';";

        ResultSet result = executeQuery(query, keyspaceId);
        Row row = result.one();

        if (row == null) return 0l;

        return row.getLong(0);

    }

    protected Boolean reset(String keyspaceId, String counter){

        Long value = get(keyspaceId, counter);

        return update(keyspaceId,counter,"-",value);

    }

    private ResultSet executeQuery(String query, String keyspaceId){
        if (keyspaceId.equalsIgnoreCase("research"))
            return dbSessionManager.getCommonSession().execute(query);
        else
            return dbSessionManager.getDomainSession(keyspaceId).execute(query);
    }

}
