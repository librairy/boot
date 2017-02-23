/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class EventBusHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusHandlerTest.class);


    private ConcurrentHashMap<String,ScheduledFuture<?>> buildingTasks;

    private ThreadPoolTaskScheduler threadpool;

    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ssZ");

    public EventBusHandlerTest(){
        this.buildingTasks = new ConcurrentHashMap<>();

        this.threadpool = new ThreadPoolTaskScheduler();
        this.threadpool.setPoolSize(500);

        this.threadpool.initialize();
    }


    public void train(String domainUri, long delay){
        LOG.info("Scheduled creation of a new task for the source: " + domainUri + " at " + timeFormatter.format(new Date(System.currentTimeMillis() + delay)));
        ScheduledFuture<?> task = buildingTasks.get(domainUri);
        if (task != null) {
            task.cancel(true);
//            this.threadpool.getScheduledThreadPoolExecutor().purge();
        }
        task = this.threadpool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Executing task!!!!!!!!!!! " + System.currentTimeMillis());
            }
        }, new Date(System.currentTimeMillis() + delay));
        buildingTasks.put(domainUri,task);

    }

}
