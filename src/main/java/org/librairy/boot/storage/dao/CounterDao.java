/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.Row;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class CounterDao extends AbstractDao {

    private static final String DEFAULT_DOMAIN = "librairy";

    private static final Logger LOG = LoggerFactory.getLogger(CounterDao.class);

    private static final String TABLE_NAME = "counters_by_domain";

    public Boolean truncate(){
        Boolean result = execute("truncate table " + TABLE_NAME );
        LOG.info("Reset global counters");
        return result;
    }

    public Boolean truncate(String domainUri){
        Boolean result = execute("delete from " + TABLE_NAME + " where domain='" + domainUri + "';");
        LOG.info("Reset counters of '" + domainUri+"'");
        return result;
    }

    public Boolean increment(String counter){
        return increment(counter,1l);
    }

    public Boolean increment(String counter, Long value){
        return update(DEFAULT_DOMAIN,counter,"+",value);
    }

    public Boolean increment(String domainUri, String counter){
        return increment(domainUri,counter,1l);
    }

    public Boolean increment(String domainUri, String counter, Long value){
        return update(domainUri,counter,"+",value);
    }

    public Boolean decrement(String counter){
        return decrement(counter,1l);
    }

    public Boolean decrement(String counter, Long value){
        return update(DEFAULT_DOMAIN,counter,"-",value);
    }

    public Boolean decrement(String domainUri, String counter){
        return decrement(domainUri,counter,1l);
    }

    public Boolean decrement(String domainUri, String counter, Long value){
        return update(domainUri,counter,"-",value);
    }

    public Long getValue(String counter){
        return get(DEFAULT_DOMAIN,counter);
    }

    public Long getValue(String domainUri, String counter){
        return get(domainUri,counter);
    }

    public Boolean reset(String counter){
        return reset(DEFAULT_DOMAIN,counter);
    }

    public Boolean reset(String domainUri, String counter){
        return update(domainUri,counter,"-",get(domainUri, counter));
    }

    private Boolean update(String domainUri, String counter, String op, Long value){
        return execute("update " + TABLE_NAME + " set value = value "+op+" "+value+" where domain='" + domainUri + "' and counter='"+counter+"';");
    }

    public Boolean remove(String counter){
        return remove(DEFAULT_DOMAIN, counter);
    }

    public Boolean remove(String domainUri, String counter){
        return execute("delete from " + TABLE_NAME + " where domain='" + domainUri+"' and counter='" + counter+"';");
    }

    private Long get(String domainUri, String counter){

        String query = "select value from "+ TABLE_NAME + " where domain='"+domainUri + "' and counter='"+counter+"';";

        Optional<Row> result = oneQuery(query);

        if (!result.isPresent()) return 0l;

        return result.get().getLong(0);

    }

    public Iterator<Row> listCounters(){
        return listCounters(DEFAULT_DOMAIN);
    }

    public Iterator<Row> listCounters(String domainUri){
        return iteratedQuery("select counter from "+ TABLE_NAME + " where domain='" + domainUri+"'");
    }
}
