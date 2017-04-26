/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage;

import com.datastax.driver.core.querybuilder.Select;
import org.librairy.boot.eventbus.EventBusHelper;
import org.librairy.boot.eventbus.RelationEventHandler;
import org.librairy.boot.eventbus.ResourceEventHandler;
import org.librairy.boot.model.domain.LinkableElement;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.storage.actions.*;
import org.librairy.boot.storage.system.column.templates.ColumnTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

    private static final Logger LOG = LoggerFactory.getLogger(UDM.class);

    @Autowired
    Helper helper;

    @Autowired
    EventBusHelper eventBusHelper;

    @Autowired
    ColumnTemplate columnTemplate;

    @Autowired
    EventBus eventBus;

    @PostConstruct
    public void setup(){
        LOG.info("Event-Bus initialized: " + eventBus);
        LOG.info("UDM initialized: " + this);
    }

    /**
     * Relation Event Handler
     * @param type
     * @param state
     * @param label
     * @param handler
     */
    public void listenFor(Relation.Type type, Relation.State state, String label, RelationEventHandler handler){
        eventBusHelper.subscribe(type, state, label, handler);
    }

    /**
     * Resource Event Handler
     * @param type
     * @param state
     * @param label
     * @param handler
     */
    public void listenFor(Resource.Type type, Resource.State state, String label, ResourceEventHandler handler){
        eventBusHelper.subscribe(type, state, label, handler);
    }


    /**
     * Native Column Queries
     * @param select
     * @return
     */
    public List<LinkableElement> queryColumn(Select select){
        return columnTemplate.query(select);
    }


    /**
     * Save a resource
     * @param resource
     */
    public SaveResourceAction save(Resource resource){return new SaveResourceAction(helper,resource);
    }

    /**
     * Save a resource
     * @param resource
     */
    public UpdateResourceAction update(Resource resource){return new UpdateResourceAction(helper,resource);
    }

    /**
     * Save a relation
     * @param relation
     */
    public SaveRelationAction save(Relation relation){
        return new SaveRelationAction(helper,relation);
    }

    /**
     * Check if a 'type' resource identified by 'uri' exists
     * @param type
     * @return boolean
     */
    public ExistsResourceAction exists(Resource.Type type){
        return new ExistsResourceAction(helper,type);
    }

    /**
     * Check if a 'type' relation identified by 'uri' exists
     * @param type
     * @return boolean
     */
    public ExistsRelationAction exists(Relation.Type type){
        return new ExistsRelationAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return resource
     */
    public ReadResourceAction read(Resource.Type type){
        return new ReadResourceAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return relation
     */
    public ReadRelationAction read(Relation.Type type){
        return new ReadRelationAction(helper,type);
    }


    /**
     * Search resources
     * @param type
     * @return uris
     */
    public SearchResourceAction find(Resource.Type type){
        return new SearchResourceAction(helper,type);
    }

    /**
     * Search relations
     * @param type
     * @return uris
     */
    public SearchRelationAction find(Relation.Type type){
        return new SearchRelationAction(helper,type);
    }

    /**
     * Delete 'type' resources
     * @param type
     * @return
     */
    public DeleteResourceAction delete(Resource.Type type){
        return new DeleteResourceAction(helper,type);
    }

    /**
     * Delete 'type' relations
     * @param type
     * @return
     */
    public DeleteRelationAction delete(Relation.Type type){
        return new DeleteRelationAction(helper,type);
    }

}
