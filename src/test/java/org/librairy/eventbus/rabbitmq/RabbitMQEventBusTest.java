package org.librairy.eventbus.rabbitmq;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.Config;
import org.librairy.eventbus.EventBusConfig;
import org.librairy.model.modules.BindingKey;
import org.librairy.model.modules.EventBus;
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
        "librairy.eventbus.host=drinventor.dia.fi.upm.es",
        "librairy.eventbus.port=5041",
        "librairy.eventbus.user=librairy",
        "librairy.eventbus.password=drinventor",
        "librairy.eventbus.keyspace=drinventor",
        "librairy.eventbus.protocol = amqp" })
public class RabbitMQEventBusTest {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQEventBusTest.class);

    @Autowired
    EventBus eventBus;

    @Test
    public void OneToOneCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.DOCUMENT, org.librairy.model.domain.resources.Resource.State.UPDATED),"test1"));

        this.eventBus.post(org.librairy.model.Event.from("test-message"), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.DOCUMENT, org.librairy.model.domain.resources.Resource.State.UPDATED));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }


    @Test
    public void OneToMoreExclusiveCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("[1] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.UPDATED),"test2"));

        this.eventBus.subscribe(event -> {
            LOG.info("[2] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.UPDATED),"test2"));

        this.eventBus.post(org.librairy.model.Event.from("test-message"), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.UPDATED));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }

    @Test
    public void OneToMoreBroadcastCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("[1] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.UPDATED),"test3.1"));

        this.eventBus.subscribe(event -> {
            LOG.info("[2] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.UPDATED),"test3.2"));

        this.eventBus.post(org.librairy.model.Event.from("test-message"), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.UPDATED));

        Thread.sleep(500);

        Assert.assertEquals(2, received.get());
    }


    @Test
    public void OneToOneNonPersistentCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.post(org.librairy.model.Event.from("test-message"), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.TOPIC, org.librairy.model.domain.resources.Resource.State.CREATED));

        Thread.sleep(500);

        this.eventBus.subscribe(event -> {
            LOG.info(" New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.TOPIC, org.librairy.model.domain.resources.Resource.State.CREATED), "test5"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }


    @Test
    public void OneToOneByURI() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final URI uri    = URI.create("http://librairy.org/source/1213-1213");

        this.eventBus.post(org.librairy.model.Event.from(uri), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.CREATED));

        Thread.sleep(500);

        this.eventBus.subscribe(event -> {
            URI eventURI = event.to(URI.class);
            LOG.info(" New event received: " + eventURI);
            Assert.assertEquals(uri, eventURI);
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.CREATED), "test6"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }

    @Test
    public void OneToOneByObject() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final Double value = new Double("23.30");

        this.eventBus.post(org.librairy.model.Event.from(value), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.DOCUMENT, org.librairy.model.domain.resources.Resource.State.UPDATED));

        Thread.sleep(500);


        this.eventBus.subscribe(event -> {
            Double eventDouble = event.to(Double.class);
            LOG.info(" New event received: " + eventDouble);
            Assert.assertEquals(value, eventDouble);
            received.incrementAndGet();
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.DOCUMENT, org.librairy.model.domain.resources.Resource.State.UPDATED), "test7"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }



}
