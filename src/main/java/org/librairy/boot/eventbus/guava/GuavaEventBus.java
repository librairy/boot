/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.modules.BindingKey;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.EventBusSubscriber;
import org.librairy.boot.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@Conditional(GuavaCondition.class)
public class GuavaEventBus implements EventBus {

	private static final Logger LOG = LoggerFactory.getLogger(GuavaEventBus.class);

	private AsyncEventBus bus = null;

	private DeadEventsSubscriber deadEventsSubscriber = new DeadEventsSubscriber();

	public GuavaEventBus() {
	}

	@PostConstruct
	public void init() {
		LOG.info("Initializing Guava Event-Bus");
		this.bus = new AsyncEventBus(java.util.concurrent.Executors.newCachedThreadPool());
		this.bus.register(this.deadEventsSubscriber);
		LOG.info("Event-Bus initialized successfully");
	}

	@PreDestroy
	public void destroy() {
		LOG.info("Guava Event-Bus closed");
	}

	@Override
	public void subscribe(EventBusSubscriber subscriber, BindingKey bindingKey) {
		LOG.debug("Subscribing " + subscriber);
		//TODO Create one AsyncEventBus for each bindingKey.routingKey value
		this.bus.register(subscriber);
	}

	@Override
	public void subscribe(EventBusSubscriber subscriber, BindingKey bindingKey, Boolean durable) {
		subscribe(subscriber, bindingKey);
	}

	@Override
	public void unsubscribe(EventBusSubscriber subscriber) {
		LOG.debug("Unsubscribing " + subscriber);
		this.bus.unregister(subscriber);
	}

	@Override
	public void unsubscribe(EventBusSubscriber subscriber, Boolean delete) {
		LOG.debug("Unsubscribing " + subscriber);
		this.bus.unregister(subscriber);
	}

	@Override
	public void post(Event event, RoutingKey key) {
		LOG.debug("Post event:[" + event + "] to: " + key + "]");
		this.bus.post(event);
	}

	@Override
	public void publish(String msg, String queue) throws IOException, TimeoutException {
		Event event = Event.from(msg);
		RoutingKey key = RoutingKey.of(queue);
		LOG.debug("Direct Post event:[" + event + "] to: " + key + "]");
		this.bus.post(event);
	}

	class DeadEventsSubscriber {
		@AllowConcurrentEvents
		@Subscribe
		public void handleDeadEvent(DeadEvent deadEvent) {
			LOG.warn("Dead event handling of:[" + deadEvent.getEvent() + "]");
			//TODO Implement logic for unrouted messages
		}
	}

}


