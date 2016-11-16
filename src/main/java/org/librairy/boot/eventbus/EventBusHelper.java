/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus;

import org.librairy.boot.model.modules.EventBusSubscriber;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.BindingKey;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class EventBusHelper {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusHelper.class);

    @Autowired
    EventBus eventBus;

    public void subscribe(Relation.Type type, Relation.State state, String label, RelationEventHandler handler){
        BindingKey bindingKey = BindingKey.of(RoutingKey.of(type, state), label);
        LOG.info("Trying to register as subscriber of '" + bindingKey + "' the following handler: " + handler);
        eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public void handle(Event event) {
                handler.handle(event.to(Relation.class));
            }
        }, bindingKey);
        LOG.info("registered successfully");
    }

    public void subscribe(Resource.Type type, Resource.State state, String label, ResourceEventHandler handler){
        BindingKey bindingKey = BindingKey.of(RoutingKey.of(type, state), label);
        LOG.info("Trying to register as subscriber of '" + bindingKey + "' the following handler: " + handler);
        eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public void handle(Event event) {
                handler.handle(event.to(Resource.class));
            }
        }, bindingKey);
        LOG.info("registered successfully");
    }
}
