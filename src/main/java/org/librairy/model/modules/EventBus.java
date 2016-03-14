package org.librairy.model.modules;

import org.librairy.model.Event;

public interface EventBus {

	void subscribe(org.librairy.model.modules.EventBusSubscriber subscriber, BindingKey bindingKey);

	void unsubscribe(org.librairy.model.modules.EventBusSubscriber subscriber);

	void post(Event event, RoutingKey routingKey);

}