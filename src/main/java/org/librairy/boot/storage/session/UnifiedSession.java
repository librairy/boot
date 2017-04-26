/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedSession  {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedSession.class);

    @PostConstruct
    public void setup(){

    }

    public void clean(){

    }


    public UnifiedTransaction beginTransaction(){
        UnifiedTransaction transaction = new UnifiedTransaction();
//        //TODO Add Cassandra Transaction
        return transaction;
    }

}
