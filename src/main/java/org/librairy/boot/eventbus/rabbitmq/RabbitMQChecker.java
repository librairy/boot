/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus.rabbitmq;

import org.librairy.boot.utils.ResourceWaiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component("busChecker")
@Conditional(RabbitMQCondition.class)
public class RabbitMQChecker {

    @Value("#{environment['LIBRAIRY_EVENTBUS_HOST']?:'${librairy.eventbus.host}'}")
    private String host;

    @Value("#{environment['LIBRAIRY_EVENTBUS_PORT']?:'${librairy.eventbus.port}'}")
    private Integer port;

    @PostConstruct
    public void setup(){
        ResourceWaiter.waitFor(host, port);
    }
}
