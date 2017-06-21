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
import org.librairy.boot.model.domain.resources.Annotation;
import org.librairy.boot.model.domain.resources.Listener;
import org.librairy.boot.storage.exception.DataNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ListenersDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(ListenersDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    public List<Listener> getByRoute(String route) throws DataNotFound {
        String query = "select "+Listener.URI +
                ", " + Listener.CREATION_TIME +
                " from listeners where " + Listener.ROUTE + "='" + route+ "' ALLOW FILTERING;";

        try {
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream()
                    .map( row -> {
                        Listener listener = new Listener();
                        listener.setRoute(route);
                        listener.setUri(row.getString(0));
                        listener.setCreationTime(row.getString(1));
                        return listener;
                    }).collect(Collectors.toList());

        } catch (InvalidQueryException e) {
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }

    }


    public Boolean removeByRoute(String route) throws DataNotFound {

        return getByRoute(route)
                .stream()
                .map( listener -> dbSessionManager.getCommonSession().execute("delete from listeners where uri='" + listener.getUri() + "';").wasApplied())
                .reduce( (r1,r2) -> r1 && r2)
                .get()
        ;
    }

    public Boolean truncate(){
        return Boolean.valueOf(this.dbSessionManager.getCommonSession().execute("truncate listeners;").wasApplied());
    }


}
