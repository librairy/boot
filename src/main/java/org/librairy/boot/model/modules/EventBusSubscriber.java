/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.modules;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.librairy.boot.model.Event;

public interface EventBusSubscriber {

    @AllowConcurrentEvents
    @Subscribe
    void handle(Event event);

}
