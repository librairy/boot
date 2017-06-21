/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class CounterDao extends AbstractCounterDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(CounterDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    public Boolean initialize(){
        Boolean result = super.initialize(DEFAULT_KEYSPACE);
        LOG.info("Initialized global counters");
        return result;
    }

    public Boolean initialize(String domainUri){
        Boolean result = super.initialize(URIGenerator.retrieveId(domainUri));
        LOG.info("Initialized counters for domain: '"+domainUri+"");
        return result;
    }

    public Boolean truncate(){
        Boolean result = super.truncate(DEFAULT_KEYSPACE);
        LOG.info("Reset global counters");
        return result;
    }

    public Boolean truncate(String domainUri){
        Boolean result = super.truncate(URIGenerator.retrieveId(domainUri));
        LOG.info("Reset counters for domain: '"+domainUri+"");
        return result;
    }

    public Boolean remove(){
        Boolean result = super.remove(DEFAULT_KEYSPACE);
        LOG.info("Removed global counters");
        return result;
    }

    public Boolean remove(String domainUri){
        Boolean result = super.remove(URIGenerator.retrieveId(domainUri));
        LOG.info("Removed counters for domain: '"+domainUri+"");
        return result;
    }

    public Boolean increment(String counter){
        return increment(counter,1l);
    }

    public Boolean increment(String counter, Long value){
        return update(DEFAULT_KEYSPACE,counter,"+",value);
    }

    public Boolean increment(String domainUri, String counter){
        return increment(domainUri,counter,1l);
    }

    public Boolean increment(String domainUri, String counter, Long value){
        return update(URIGenerator.retrieveId(domainUri),counter,"+",value);
    }

    public Boolean decrement(String counter){
        return decrement(counter,1l);
    }

    public Boolean decrement(String counter, Long value){
        return update(DEFAULT_KEYSPACE,counter,"-",value);
    }

    public Boolean decrement(String domainUri, String counter){
        return decrement(domainUri,counter,1l);
    }

    public Boolean decrement(String domainUri, String counter, Long value){
        return update(URIGenerator.retrieveId(domainUri),counter,"-",value);
    }

    public Long getValue(String counter){
        return get(DEFAULT_KEYSPACE,counter);
    }

    public Long getValue(String domainUri, String counter){
        return get(URIGenerator.retrieveId(domainUri),counter);
    }

    public Boolean reset(String counter){
        return super.reset(DEFAULT_KEYSPACE,counter);
    }

    public Boolean reset(String domainUri, String counter){
        return super.reset(URIGenerator.retrieveId(domainUri),counter);
    }

}
