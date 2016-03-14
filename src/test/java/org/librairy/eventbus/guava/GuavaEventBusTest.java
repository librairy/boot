package org.librairy.eventbus.guava;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.librairy.Config;
import org.librairy.eventbus.EventBusConfig;
import org.librairy.model.modules.EventBus;
import org.librairy.model.modules.EventBusSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 14/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EventBusConfig.class)
@TestPropertySource(properties = { "librairy.eventbus.protocol = localhost" })
public class GuavaEventBusTest {

    private static final Logger LOG = LoggerFactory.getLogger(GuavaEventBus.class);

    @Autowired
    EventBus eventBus;


    @Test
    public void OneToOneCommunication() throws IOException, InterruptedException {

        LOG.info("Starting the bus test");

        final AtomicInteger counter = new AtomicInteger(0);

        eventBus.post(org.librairy.model.Event.from("nobody should be listening"), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.ITEM, org.librairy.model.domain.resources.Resource.State.CREATED));

        eventBus.subscribe(new EventBusSubscriber() {

            @Override
            public void handle(org.librairy.model.Event event) {
                LOG.info("Reacting to the event ! " + event.to(String.class));
                counter.incrementAndGet();
            }
        }, org.librairy.model.modules.BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.ITEM, org.librairy.model.domain.resources.Resource.State.CREATED),"test"));

        eventBus.post(org.librairy.model.Event.from((new Date()).toString()), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.ITEM, org.librairy.model.domain.resources.Resource.State.CREATED));

        Thread.sleep(100);

        Assert.assertEquals(1,counter.get());
    }

}
