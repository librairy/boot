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
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.exception.DataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class DomainsDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(DomainsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    public Boolean exists(String domainUri, String resourceUri){
        String query = "select count(*) from contains where starturi='" + domainUri + "' and enduri='"+resourceUri+"' ALLOW FILTERING;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if ((row == null) || row.getLong(0) < 1) return false;

        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
        return true;
    }

    public Iterator<Row> listFrom(String resourceUri){
        String query = "select starturi from contains where enduri='"+resourceUri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Iterator<Row> iterator = result.iterator();

            if ((iterator == null)) return Collections.emptyIterator();

            return iterator;
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyIterator();
        }
    }

    public List<String> listOnly(String field){
        String query = "select "+field+" from domains;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream().map(row -> row.getString(0)).collect(Collectors.toList());
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }

    }

    public List<Domain> listAll(){
        String query = "select uri, name, description, creationtime from domains;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream().map(row -> {
                Domain domain = new Domain();
                domain.setUri(row.getString(0));
                domain.setName(row.getString(1));
                domain.setDescription(row.getString(2));
                domain.setCreationTime(row.getString(3));
                return domain;
            }).collect(Collectors.toList());
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Domain get(String uri) throws DataNotFound {
        String query = "select name, description, creationtime from domains where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if ((row == null)) throw new DataNotFound("No domain found by '" + uri + "'");

            Domain domain = new Domain();
            domain.setName(row.getString(0));
            domain.setDescription(row.getString(1));
            domain.setCreationTime(row.getString(2));
            domain.setUri(uri);

            return domain;
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            throw new DataNotFound("No domain found by '" + uri + "'");
        }
    }


    public Long count(String uri, String element) throws DataNotFound {
        String query = "select num from counts where name='" + element + "' ALLOW FILTERING;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(uri).execute(query);
            Row row = result.one();

            if ((row == null)) return 0l;

            return row.getLong(0);
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            throw new DataNotFound("No domain found by '" + uri + "'");
        }
    }



}
