/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.BindingKey;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class SubdomainsDao extends AbstractDao {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(SubdomainsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    @Autowired
    EventBus eventBus;

    @Autowired
    CounterDao counterDao;


    public Boolean initialize(String domainUri){
        String query = "create table if not exists subdomains(uri varchar, name text, time text, primary key(uri));";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("Initialized subdomains table for '"+domainUri+"'");
            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }


    public Boolean exists(String domainUri, String subdomainUri){
        String query = "select count(uri) from subdomains where uri='" + subdomainUri+ "' ;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            Row row = result.one();

            if ((row == null) || row.getLong(0) < 1) return false;

        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
        return true;
    }


    public List<Domain> list(String domainUri, Integer max, String offset, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select uri, name, time from subdomains");

        if (offset != null){
            String operator = inclusive? ">=" : ">";
            query.append(" token(uri) "+operator+" token('" + URIGenerator.fromId(
                    Resource.Type.DOMAIN,offset) + "')");
        }

        query.append(" limit ").append(max);

        query.append(";");

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query.toString());
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream().map(row -> {
                Domain domain = new Domain();
                domain.setUri(row.getString(0));
                domain.setName(row.getString(1));
                domain.setCreationTime(row.getString(2));
                return domain;
            }).collect(Collectors.toList());
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean save(String domainUri, Domain subdomain){
        String query = "insert into subdomains (uri,name,time) values('"+subdomain.getUri()+"', '"+escaper.escape(subdomain.getName())+"', '"+ TimeUtils.asISO()+"');";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("saved subdomain '"+subdomain.getUri()+"' in '"+domainUri+"'");

            // increment counter
            counterDao.increment(domainUri, Resource.Type.DOMAIN.route());

            //publish event
            updateDomain(domainUri, subdomain.getUri(),"added");

            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String domainUri, String subdomainUri){
        String query = "delete from subdomains where uri='"+subdomainUri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("delete subdomain '"+subdomainUri+"' from '"+domainUri+"'");

            // decrement counter
            counterDao.decrement(domainUri, Resource.Type.DOMAIN.route());

            //publish event
            updateDomain(domainUri, subdomainUri, "deleted");

            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAll(String domainUri){
        String query = "drop table subdomains;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(domainUri).execute(query);
            LOG.info("deleted all subdomains from '"+domainUri+"'");

            initialize(domainUri);

            // decrement counter
            counterDao.reset(domainUri, Resource.Type.DOMAIN.route());

            //TODO publish event

            return result.wasApplied();
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
            return false;
        }
    }

    private void updateDomain(String domainUri, String subdomainUri, String action){
        eventBus.post(Event.from(Relation.newContains(domainUri, subdomainUri)), RoutingKey.of("subdomain."+action));
    }

}
