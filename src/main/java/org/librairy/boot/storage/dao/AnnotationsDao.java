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
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    @Autowired
    UDM udm;

    public List<Annotation> getByResource(String uri, Optional<AnnotationFilter> filter) throws DataNotFound {
        String query = "select "+Annotation.URI +
                " from annotations_by_resource where " + Annotation.RESOURCE + "='" + uri + "' ";

        if (filter.isPresent()){

            if (filter.get().type.isPresent()){
                query += " and "+ Annotation.TYPE + "='" + filter.get().type.get() + "' ";

            }

            if (filter.get().purpose.isPresent()){
                query += " and "+ Annotation.PURPOSE + "='" + filter.get().purpose.get() +"' ";

            }

            if (filter.get().creator.isPresent()){
                query += " and "+ Annotation.CREATOR + "='" + filter.get().creator.get() +"' ";
            }
        }


        query += " ;";

        try {
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream()
                    .map( row -> udm.read(Resource.Type.ANNOTATION).byUri(row.getString(0)).get().asAnnotation())
                    .collect(Collectors.toList());

        } catch (InvalidQueryException e) {
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }

    }


    public Boolean removeByResource(String resourceUri, Optional<AnnotationFilter> filter) throws DataNotFound {

        try{
            getByResource(resourceUri, filter).stream().forEach( annotation -> udm.delete(Resource.Type.ANNOTATION).byUri(annotation.getUri()));
            return true;
        }catch (Exception e){
            LOG.error("Error removing annotation by resource: " + resourceUri,e);
            return false;
        }
    }

    public Boolean removeByUri(String annotationUri) throws DataNotFound {

        try{
            Optional<Resource> annotation = udm.read(Resource.Type.ANNOTATION).byUri(annotationUri);

            if (!annotation.isPresent()) {
                LOG.warn("Annotation not found by id: '" + annotationUri + "'");
                return false;
            }

            AnnotationFilter filter = AnnotationFilter
                    .byType(annotation.get().asAnnotation().getType())
                    .byPurpose(annotation.get().asAnnotation().getPurpose())
                    .byCreator(annotation.get().asAnnotation().getCreator())
                    .build();

            removeByResource(annotation.get().asAnnotation().getResource(), Optional.of(filter));
            return true;
        }catch (Exception e){
            LOG.error("Error removing annotation by id: " + annotationUri,e);
            return false;
        }
    }


    public Boolean truncate(){

        return Arrays.asList(new String[]{"annotations","annotations_by_resource"}).stream()
                .map( table -> Boolean.valueOf(this.dbSessionManager.getCommonSession().execute("truncate "+table+";").wasApplied()))
                .reduce((a,b) -> a&b).get();
    }


}
