/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.modules;

import org.librairy.boot.model.Event;

public interface EventBus {

	void subscribe(EventBusSubscriber subscriber, BindingKey bindingKey);

	void unsubscribe(EventBusSubscriber subscriber);

	void post(Event event, RoutingKey routingKey);

}