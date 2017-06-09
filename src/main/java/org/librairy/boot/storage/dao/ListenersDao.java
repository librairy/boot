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
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Listener;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ListenersDao extends AbstractDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(ListenersDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    @Autowired
    EventBus eventBus;

    @PostConstruct
    public Boolean initialize(){
        String query = "create table if not exists research.listeners(id text, time text, primary key (id));";

        ResultSet result = dbSessionManager.getCommonSession().execute(query);

        return result.wasApplied();
    }


    public Boolean save(String id){
        String query = "insert into listeners(id,time) values('"+id+"', '"+ TimeUtils.asISO()+"');";

        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            LOG.debug("Added listener for '"+id+"'");
            eventBus.post(Event.from(id), RoutingKey.of("listener.created"));
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    public Listener get(String id) throws DataNotFound {
        String query = "select time from listeners where id='"+id+"';";

        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            Row row = result.one();

            if (row == null) throw new DataNotFound("Not found '"+id+"'");

            Listener listener = new Listener();
            listener.setId(id);
            listener.setTime(row.getString(0));
            return listener;
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return new Listener();
        }

    }

    public Boolean remove(String id){
        String query = "delete from listeners where id='"+id+"';";

        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            return result.wasApplied();
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
    }

    public Boolean removeAll(){
        String query = "truncate listeners;";

        try{
            ResultSet result = dbSessionManager.getCommonSession().execute(query);
            return result.wasApplied();
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
    }

    public List<Listener> list(Integer size, Optional<String> offset, Boolean inclusive) {
        StringBuilder query = new StringBuilder().append("select id, time from listeners");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" where token(id) "+operator+" token('" + offset.get() + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        Iterator<Row> it = super.iteratedQuery(query.toString());

        List<Listener> listeners= new ArrayList<>();
        while(it.hasNext()){
            Row row = it.next();
            Listener listener = new Listener();
            listener.setId(row.getString(0));
            listener.setTime(row.getString(1));
            listeners.add(listener);
        }

        return listeners;
    }


}
