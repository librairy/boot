/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus.rabbitmq;

import com.rabbitmq.client.Channel;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.modules.BindingKey;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.EventBusSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Created by cbadenes on 09/10/15.
 */
@Component
@Conditional(RabbitMQCondition.class)
public class RabbitMQEventBus implements EventBus {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQEventBus.class);

    private static final String EXCHANGE = "librairy.eventbus";

    @Value("#{environment['LIBRAIRY_EVENTBUS_HOST']?:'${librairy.eventbus.host}'}")
    private String host;

    @Value("#{environment['LIBRAIRY_EVENTBUS_PORT']?:'${librairy.eventbus.port}'}")
    private String port;

    @Value("#{environment['LIBRAIRY_EVENTBUS_USER_NAME']?:'${librairy.eventbus.user.name}'}")
    private String user;

    @Value("#{environment['LIBRAIRY_EVENTBUS_USER_PWD']?:'${librairy.eventbus.user.pwd}'}")
    private String pwd;

    @Value("#{environment['LIBRAIRY_EVENTBUS_KEYSPACE']?:'${librairy.eventbus.keyspace}'}")
    private String keyspace;

    private Channel channel;

    private RabbitMQClient client;

    @PostConstruct
    public void init() {
        try {
            String uri = new StringBuilder().
                    append("amqp://").append(user).append(":").append(pwd).
                    append("@").append(host).append(":").append(port).
                    append("/").append(keyspace).toString();
            LOG.info("Initializing RabbitMQ Event-Bus in: " + uri);
            this.client = new RabbitMQClient();
//            this.client.connect(uri);
            this.client.connect(user,pwd, host, Integer.valueOf(port), keyspace);
            this.channel = this.client.newChannel(EXCHANGE);
            LOG.info("RabbitMQ Event-Bus initialized successfully");
        } catch (IOException | TimeoutException | NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            this.client.disconnect();
        } catch (TimeoutException e) {
            LOG.warn("Timeout trying to disconnect from MessageBroker");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(EventBusSubscriber subscriber, BindingKey bindingKey) {
        try {
            LOG.debug("subscribing: " + subscriber + " to: " + bindingKey);
            this.client.consume(EXCHANGE, bindingKey.getGroup(), bindingKey.getKey(), subscriber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unsubscribe(EventBusSubscriber subscriber) {
        try {
            LOG.debug("unsubscribing: " + subscriber);
            this.client.clean(subscriber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOG.warn("Timeout trying to close subscriber: " + subscriber);
        }
    }

    @Override
    public void post(Event event, RoutingKey routingKey) {
        try {
            LOG.debug("post event: " + event + " to: " + routingKey);
            this.client.publish(channel, EXCHANGE, routingKey.getKey(),event.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void directPost(String msg, String queue) throws IOException, TimeoutException {

        Channel channel = client.connection.createChannel();
        channel.queueDeclare(queue, false, false, false, null);
        channel.basicPublish("", queue, null, msg.getBytes());
        LOG.info("Sent '" + msg + "' to queue: '"+ queue + "' in event-bus");
        channel.close();
    }
}
