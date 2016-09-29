/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.eventbus.rabbitmq;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.eventbus.EventBusConfig;
import org.librairy.model.Event;
import org.librairy.model.domain.relations.Contains;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.modules.BindingKey;
import org.librairy.model.modules.EventBus;
import org.librairy.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 13/10/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EventBusConfig.class)
@TestPropertySource(properties = {
        "librairy.eventbus.host=192.168.99.100"
})
public class EventBusTest {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusTest.class);

    @Autowired
    EventBus eventBus;


    @Test
    public void deploy(){

        Assert.assertTrue(true);
    }

}
