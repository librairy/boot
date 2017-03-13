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
import org.librairy.boot.model.Annotation;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class AnnotationsDao extends AbstractDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    @Autowired
    EventBus eventBus;

    @PostConstruct
    public Boolean initialize(){
        String query = "create table if not exists annotations(uri varchar, type varchar, time varchar, value text, primary key(uri,type));";

        ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);

        return result.wasApplied();
    }


    public Boolean saveOrUpdate(String uri, String type, String value){
        String query = "insert into annotations(uri,type,time,value) values('"+uri+"', '"+type+"', '"+ TimeUtils.asISO()+"', '"+escaper.escape(value)+"');";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            LOG.debug("Annotated '"+uri+"' with '"+type+"'");
            //publish event
            Resource resource = new Resource();
            resource.setUri(uri);
            eventBus.post(Event.from(resource), RoutingKey.of(URIGenerator.typeFrom(uri), Resource.State.UPDATED));
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    public Annotation get(String uri, String type) throws DataNotFound {
        String query = "select time,value from annotations where uri='"+uri+"' and type='"+type+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if (row == null) throw new DataNotFound("Not found '"+type+"' in '"+uri+"'");

            Annotation annotation = new Annotation();
            annotation.setType(type);
            annotation.setTime(row.getString(0));
            annotation.setValue(row.getString(1));
            return annotation;
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return new Annotation();
        }

    }

    public List<Annotation> list(String uri, Boolean content){
        String query = "select type, time";

        if(content){
            query += ", value";
        }

        query += " from annotations where uri='" + uri + "';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream().map(row -> {
                Annotation annotation = new Annotation();
                annotation.setType(row.getString(0));
                annotation.setTime(row.getString(1));

                if (content) annotation.setValue(row.getString(2));

                return annotation;
            }).collect(Collectors.toList());
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Boolean remove(String uri, String type){
        String query = "delete from annotations where uri='"+uri+"' and type='"+type+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            return result.wasApplied();
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
    }

    public Boolean removeAll(String uri){
        String query = "delete from annotations where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            return result.wasApplied();
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
    }

    public Boolean removeAll(){
        String query = "truncate annotations;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            return result.wasApplied();
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
    }


}
