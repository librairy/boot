package org.librairy.eventbus.rabbitmq;

import com.rabbitmq.client.Channel;
import org.librairy.model.Event;
import org.librairy.model.modules.BindingKey;
import org.librairy.model.modules.EventBus;
import org.librairy.model.modules.EventBusSubscriber;
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

    @Value("${librairy.eventbus.host}")
    private String host;

    @Value("${librairy.eventbus.port}")
    private String port;

    @Value("${librairy.eventbus.user}")
    private String user;

    @Value("${librairy.eventbus.password}")
    private String pwd;

    @Value("${librairy.eventbus.keyspace}")
    private String keyspace;

    private Channel channel;

    private org.librairy.eventbus.rabbitmq.RabbitMQClient client;

    @PostConstruct
    public void init() {
        try {
            String uri = new StringBuilder().
                    append("amqp://").append(user).append(":").append(pwd).
                    append("@").append(host).append(":").append(port).
                    append("/").append(keyspace).toString();
            LOG.info("Initializing RabbitMQ Event-Bus in: " + uri);
            this.client = new org.librairy.eventbus.rabbitmq.RabbitMQClient();
            this.client.connect(uri);
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
    public void post(Event event, org.librairy.model.modules.RoutingKey routingKey) {
        try {
            LOG.debug("post event: " + event + " to: " + routingKey);
            this.client.publish(channel, EXCHANGE, routingKey.getKey(),event.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
