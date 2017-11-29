/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.librairy.boot.model.domain.resources.Param;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ParametersDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(ParametersDao.class);

    private static final String TABLE_NAME="parameters_by_domain";

    public Boolean initialize(String domainUri){

        try{
            //TODO this part should be moved to specific modules

            saveOrUpdate(domainUri,"tokenizer.mode","lemma");
            saveOrUpdate(domainUri,"lda.delay","30000");
            saveOrUpdate(domainUri,"lda.vocabulary.size","10000");
            saveOrUpdate(domainUri,"lda.optimizer","basic");
            saveOrUpdate(domainUri,"lda.max.evaluations","50");
            saveOrUpdate(domainUri,"lda.max.iterations","10");
            saveOrUpdate(domainUri,"lda.features","TF");
            saveOrUpdate(domainUri,"lda.topics","-1");
            saveOrUpdate(domainUri,"lda.alpha","-1");
            saveOrUpdate(domainUri,"lda.beta","-1");
            saveOrUpdate(domainUri,"lda.stopwords","figure,section,example,table");
            saveOrUpdate(domainUri,"w2v.delay","300000");
            saveOrUpdate(domainUri,"w2v.dim","100");
            saveOrUpdate(domainUri,"w2v.max.iterations","20");

            LOG.info("Initialized parameters for '"+domainUri+"'");
            return true;
        }catch (InvalidQueryException e){
            LOG.warn("Error initializing parameters for domain '" + domainUri+ "' : " + e.getMessage());
            return false;
        }

    }

    public Boolean remove(String domainUri){
        return execute("delete from "+TABLE_NAME+" where domain='"+domainUri+"';");
    }

    public Boolean saveOrUpdate(String domainUri, String name, String value){
        Boolean result = execute("insert into " + TABLE_NAME + " (domain,parameter,value) values('" + domainUri + "', '" + name + "', '" + escaper.escape(value) + "');");
        if (result) LOG.debug("Added name: '"+name+"' = '"+value+"' in '"+domainUri+"'");
        return result;
    }

    public Boolean remove(String domainUri, String parameter){
        return execute("delete from "+TABLE_NAME+" where domain='"+domainUri+"' and parameter='"+parameter+"';");
    }

    public Optional<String> get(String domainUri, String parameter) {
        Optional<Row> result = oneQuery("select value from " + TABLE_NAME + " where domain='" + domainUri + "' and parameter='" + parameter + "';");
        if (!result.isPresent()) return Optional.empty();
        return Optional.of(result.get().getString(0));

    }

    public List<Param> listAt(String domainUri){
        Iterator<Row> result = iteratedQuery("select parameter, value from " + TABLE_NAME + " where domain='" + domainUri + "';");
        List<Param> parameters = new ArrayList<>();
        while(result.hasNext()){
            Row row = result.next();

            Param parameter = new Param();
            parameter.setName(row.getString(0));
            parameter.setValue(row.getString(1));

            parameters.add(parameter);

        }

        return parameters;

    }

}
