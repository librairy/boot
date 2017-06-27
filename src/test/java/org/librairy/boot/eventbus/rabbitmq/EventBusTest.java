/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus.rabbitmq;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.eventbus.EventBusConfig;
import org.librairy.boot.eventbus.EventMessage;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.BindingKey;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.EventBusSubscriber;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by cbadenes on 13/10/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EventBusConfig.class)
//@TestPropertySource(properties = {
//        "librairy.eventbus.host=192.168.99.100"
//})
public class EventBusTest implements EventBusSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusTest.class);

    @Autowired
    EventBus eventBus;


    EventBusHandlerTest handlerTest = new EventBusHandlerTest();


    long delay = 5000;

    @Test
    public void deploy(){

        Assert.assertTrue(true);
    }


    @Test
    public void domainAnalyzed() throws IOException, TimeoutException {

        String domainUri = "http://librairy.org/domains/12345";

        EventMessage eventMessage = new EventMessage();
        eventMessage.setName("sample-name");
        eventMessage.setId("sample-id");
        eventMessage.setTime(TimeUtils.asISO());

        ObjectMapper mapper = new ObjectMapper();
        String jsonMsg = mapper.writeValueAsString(eventMessage);

        eventBus.publish(jsonMsg, "domains.analyzed");

    }


    @Test
    public void masiveCommunication(){
        BindingKey bindingKey = BindingKey.of(RoutingKey.of(Resource.Type.DOMAIN, Resource.State.CREATED), "test.boot.source.created");
        LOG.info("Trying to register as subscriber of '" + bindingKey + "' events ..");
        eventBus.subscribe(this,bindingKey );
        LOG.info("registered successfully");

        Random random = new Random();
        int numMsgs = 1000;
        for (int i=0; i<numMsgs ; i++){
            try {
                Thread.sleep(random.nextInt(10)*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Domain domain = Resource.newDomain("test");
            domain.setUri("http://librairy.org/domains/test"+ i%5);
            Event event = Event.from(domain);
            LOG.info("Sending Event: "  + event);
            eventBus.post(event, RoutingKey.of(Resource.Type.DOMAIN, Resource.State.CREATED));
        }

        try {
            LOG.info("Test sleeping..");
            Thread.sleep(Long.MAX_VALUE);
            LOG.info("Test finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void handle(Event event) {
//        LOG.info("Sleeping " + delay + "msecs ..");
//        try {
//            Thread.sleep(delay);
//            LOG.info("Event handled!: " + event);
//        } catch (InterruptedException e) {
//            LOG.error("Interrupted exception", e);
//        }
//

        try{
            Resource resource = event.to(Resource.class);

            LOG.info("Handle a new event: " + resource.getUri());
            handlerTest.train(resource.getUri(),delay);

        } catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error scheduling a new topic model from an updated domain: " + event, e);
        }


    }
}
