package org.librairy.model.modules;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public interface EventBusSubscriber {

    @AllowConcurrentEvents
    @Subscribe
    void handle(org.librairy.model.Event event);

}
