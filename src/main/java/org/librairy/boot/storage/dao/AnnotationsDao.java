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
public class AnnotationsDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    public List<Annotation> getByResource(String uri, Optional<String> type, Optional<String> purpose, Optional<String> creator) throws DataNotFound {
        String query = "select "+Annotation.URI +
                ", " + Annotation.TYPE +
                ", " + Annotation.CREATION_TIME +
                ", " + Annotation.CREATOR +
                ", " + Annotation.FORMAT +
                ", " + Annotation.LANGUAGE +
                ", " + Annotation.VALUE +
                ", " + Annotation.DESCRIPTION +
                ", " + Annotation.PURPOSE +
                ", " + Annotation.SCORE +
                ", " + Annotation.SELECTION +
                " from annotations where " + Annotation.RESOURCE + "='" + uri + "' ";

        if (type.isPresent()){
            query += " and "+ Annotation.TYPE + "='" + type.get() + "' ";
        }

        if (purpose.isPresent()){
            query += " and "+ Annotation.PURPOSE + "='" + purpose.get() +"' ";
        }

        if (creator.isPresent()){
            query += " and "+ Annotation.CREATOR + "='" + creator.get() +"' ";
        }

        query += " ALLOW FILTERING ;";

        try {
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream()
                    .map( row -> {
                        Annotation annotation = new Annotation();
                        annotation.setResource(uri);
                        annotation.setUri(row.getString(0));
                        annotation.setType(row.getString(1));
                        annotation.setCreationTime(row.getString(2));
                        annotation.setCreator(row.getString(3));
                        annotation.setFormat(row.getString(4));
                        annotation.setLanguage(row.getString(5));
                        annotation.setValue(row.getMap(6, String.class, String.class));
                        annotation.setDescription(row.getString(7));
                        annotation.setPurpose(row.getString(8));
                        annotation.setScore(row.getDouble(9));
                        annotation.setSelection(row.getMap(10, String.class, String.class));
                        return annotation;
                    }).collect(Collectors.toList());

        } catch (InvalidQueryException e) {
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }

    }


    public Boolean removeByResource(String uri, Optional<String> type, Optional<String> purpose, Optional<String> creator) throws DataNotFound {

        return getByResource(uri,type,purpose,creator)
                .stream()
                .map( annotation -> dbSessionManager.getCommonSession().execute("delete from annotations where uri='" + annotation.getUri() + "';").wasApplied())
                .reduce( (r1,r2) -> r1 && r2)
                .get()
        ;
    }


}
