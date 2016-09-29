/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.eventbus.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Conditional(GuavaCondition.class)
public class GuavaEventBus implements org.librairy.model.modules.EventBus {

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
	public void subscribe(org.librairy.model.modules.EventBusSubscriber subscriber, org.librairy.model.modules.BindingKey bindingKey) {
		LOG.debug("Subscribing " + subscriber);
		//TODO Create one AsyncEventBus for each bindingKey.routingKey value
		this.bus.register(subscriber);
	}

	@Override
	public void unsubscribe(org.librairy.model.modules.EventBusSubscriber subscriber) {
		LOG.debug("Unsubscribing " + subscriber);
		this.bus.unregister(subscriber);
	}

	@Override
	public void post(org.librairy.model.Event event, org.librairy.model.modules.RoutingKey key) {
		LOG.debug("Post event:[" + event + "] to: " + key + "]");
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


