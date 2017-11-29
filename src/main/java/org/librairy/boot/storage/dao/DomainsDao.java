/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.*;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class DomainsDao extends AbstractDao  {

    private static final Logger LOG = LoggerFactory.getLogger(DomainsDao.class);

    @Autowired
    UDM udm;

    @Autowired
    EventBus eventBus;

    @Autowired
    CounterDao counterDao;

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    ParametersDao parametersDao;

    @Autowired
    AnnotationsDao annotationsDao;




    public static final String TABLE_NAME = "resources_by_domain";

    public Boolean exists(String domainUri){
        return super.countQuery("select count(uri) from domains where uri='" + domainUri + "';");
    }


    public Boolean contains(String domainUri, String resourceUri){
        String type = URIGenerator.typeFrom(resourceUri).key();
        return countQuery("select count(resource) from " + TABLE_NAME + " where domain='"+domainUri+"' and type='"+ type+ "' and resource='" + resourceUri + "';");
    }


    public Optional<Domain> get(String domainUri)  {

        Optional<Resource> domain = udm.read(Resource.Type.DOMAIN).byUri(domainUri);

        if (!domain.isPresent()) return Optional.empty();

        return Optional.of(domain.get().asDomain());

    }

    public List<Domain> list(Integer size, Optional<String> offset, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select uri, name, description, creationtime from domains");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" where token(uri) "+operator+" token('" + URIGenerator.fromId(Resource.Type.DOMAIN, offset.get())+"') ");
        }

        query.append(" limit " + size);
        query.append(";");

        List<Domain> domains = new ArrayList<>();
        Iterator<Row> it = super.iteratedQuery(query.toString());
        while(it.hasNext()){
            Row row = it.next();
            Domain domain = new Domain();
            domain.setUri(row.getString(0));
            domain.setName(row.getString(1));
            domain.setDescription(row.getString(2));
            domain.setCreationTime(row.getString(3));
            domains.add(domain);
        }

        return domains;

    }

    public List<Item> listItems(String domainUri, Integer size, Optional<String> offset, Boolean inclusive) {

        Optional<String> uri = (offset.isPresent() && !offset.get().startsWith("http://"))? Optional.of(URIGenerator.fromContent(Resource.Type.ITEM, offset.get())) : offset;

        Iterator<Row> it = listResources(domainUri, size, uri, Resource.Type.ITEM, inclusive);

        List<Item> items = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Item item = new Item();
            item.setUri(row.getString(0));
            item.setCreationTime(row.getString(1));
            item.setDescription(row.getString(2));
            items.add(item);
        }

        return items;
    }

    public List<Part> listParts(String domainUri, Integer size, Optional<String> offset, Boolean inclusive) {

        Optional<String> uri = (offset.isPresent() && !offset.get().startsWith("http://"))? Optional.of(URIGenerator.fromId(Resource.Type.PART, offset.get())) : offset;

        Iterator<Row> it = listResources(domainUri, size, uri, Resource.Type.PART, inclusive);

        List<Part> parts = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Part part = new Part();
            part.setUri(row.getString(0));
            part.setCreationTime(row.getString(1));
            part.setSense(row.getString(2));
            parts.add(part);
        }

        return parts;
    }

    public List<Domain> listSubdomains(String domainUri, Integer size, Optional<String> offset, Boolean inclusive) {

        Optional<String> uri = (offset.isPresent() && !offset.get().startsWith("http://"))? Optional.of(URIGenerator.fromId(Resource.Type.DOMAIN, offset.get())) : offset;

        Iterator<Row> it = listResources(domainUri, size, uri, Resource.Type.DOMAIN, inclusive);

        List<Domain> domains = new ArrayList<>();

        while(it.hasNext()){
            Row row = it.next();
            Domain domain = new Domain();
            domain.setUri(row.getString(0));
            domain.setCreationTime(row.getString(1));
            domain.setName(row.getString(2));
            domains.add(domain);
        }

        return domains;
    }

    public Iterator<Row> listResources(String domainUri, Integer size, Optional<String> offset, Resource.Type type, Boolean inclusive){
        StringBuilder query = new StringBuilder().append("select resource, time, name from " + TABLE_NAME + " where domain='" + domainUri + "' and type='" + type.key() + "' ");

        if (offset.isPresent()){
            String operator = inclusive? ">=" : ">";
            query.append(" and resource "+operator+" '" + offset.get() + "'");
        }

        query.append(" limit " + size);
        query.append(";");

        return iteratedQuery(query.toString());
    }


    public boolean save(Domain domain){
        try{
            String regex = "^[a-zA-Z0-9_]+$";
            if (!domain.getName().matches(regex)) {
             LOG.warn("Invalid domain name: " + domain.getName());
                return false;
            }
            udm.save(domain);
            return true;
        }catch (Exception e){
            LOG.error("Error saving domain: " + domain);
            return false;
        }
    }


    public Boolean addItem(String domainUri, String itemUri){


        Optional<Resource> item = udm.read(Resource.Type.ITEM).byUri(itemUri);

        if (!item.isPresent()) return false;

        // add to items item
        save(domainUri, itemUri, item.get().asItem().getDescription());

        // add to contains item
        udm.save(Relation.newContains(domainUri, itemUri));

        // for each part
        Integer windowSize = 100;
        Optional<String> offset = Optional.empty();
        Boolean finished = false;

        while(!finished){

            List<Part> parts = itemsDao.listParts(itemUri, windowSize, offset, false);

            // add to parts
            for (Part part: parts){
                addPart(domainUri, part.getUri());
            }


            if (parts.size() < windowSize){
                finished = true;
            }else{
                offset = Optional.of(URIGenerator.retrieveId(parts.get(windowSize-1).getUri()));
            }
        }
        return true;
    }

    public Boolean addPart(String domainUri, String partUri){

        // add to contains
//        udm.save(Relation.newContains(domainUri, partUri));

        Optional<Resource> part = udm.read(Resource.Type.PART).byUri(partUri);

        if (!part.isPresent()) return false;

        save(domainUri, partUri, part.get().asPart().getSense());
        counterDao.increment(domainUri, Resource.Type.PART.route());
        return true;
    }

    public Boolean addSubdomain(String domainUri, String subdomainUri){

        Optional<Resource> subdomain = udm.read(Resource.Type.DOMAIN).byUri(subdomainUri);

        if (!subdomain.isPresent()) return false;

        save(domainUri, subdomainUri, subdomain.get().asDomain().getName());
        counterDao.increment(domainUri, Resource.Type.DOMAIN.route());
        eventBus.post(Event.from(Relation.newContains(domainUri, subdomainUri)), RoutingKey.of("subdomain.added"));
        return true;
    }

    private Boolean save(String domainUri, String resourceUri, String name){

        String type = URIGenerator.typeFrom(resourceUri).key();
        PreparedStatement statement = dbSessionManager.getCommonSession().prepare("insert into "+TABLE_NAME+" (domain, type, resource, time, name) values (?, ?, ?, ?, ?)");
        dbSessionManager.getCommonSession().executeAsync(statement.bind(
                domainUri,
                type,
                resourceUri,
                TimeUtils.asISO(),
                name));
        LOG.info("saved resource '"+resourceUri+"' in '"+domainUri+"'");
        return true;



    }

    private String retrieveDomainTokens(String domainUri, String resourceUri){
        //add tokens from domain parameter
        Optional<String>   tokenizerMode = parametersDao.get(domainUri, "tokenizer.mode");

        StringTokenizer tokenizer = new StringTokenizer(tokenizerMode.isPresent()? tokenizerMode.get() : "lemma", "+", false);
        String tokens = "";
        while(tokenizer.hasMoreTokens()){
            String type = tokenizer.nextToken();
            try {
                AnnotationFilter filterByType = AnnotationFilter.byType(type).build();
                for (Annotation annotation : annotationsDao.getByResource(resourceUri, Optional.of(filterByType))){
                    tokens += annotation.getValue().get("content");
                    tokens += " ";
                }

            } catch (DataNotFound dataNotFound) {
                LOG.debug("No annotation '" + type+ "' found for " + resourceUri);
            }
        }

        return Strings.isNullOrEmpty(tokens)? tokens : escaper.escape(tokens);
    }

    public Optional<String> getDomainTokens(String domainUri, String uri)  {
        String type    = URIGenerator.typeFrom(uri).key();
        Optional<Row> tokens = oneQuery("select tokens from " + TABLE_NAME + " where domain='" + domainUri + "' and type='" + type + "' and resource='" + uri + "';");
        if (!tokens.isPresent()) Optional.empty();
        return Optional.of(tokens.get().getString(0));
    }

    public Boolean updateDomainTokens(String domainUri, String uri, String name){
        return updateDomainTokens(domainUri, uri, name, retrieveDomainTokens(domainUri, uri));
    }

    public Boolean updateDomainTokens(String domainUri, String uri, String name, String tokens){

        if (Strings.isNullOrEmpty(tokens)) return true;


        String type = URIGenerator.typeFrom(uri).key();
        PreparedStatement statement = dbSessionManager.getCommonSession().prepare("insert into "+TABLE_NAME+" (domain, type, resource, tokens) values (?, ?, ?, ?)");
        dbSessionManager.getCommonSession().executeAsync(statement.bind(
                domainUri,
                type,
                uri,
                tokens));

        LOG.info("saved " + tokens.length() + " tokens of '"+uri+"' in '"+domainUri+"'");
        Domain resource = new Domain();
        resource.setUri(domainUri);
        eventBus.post(Event.from(resource), RoutingKey.of(resource.getResourceType(), Resource.State.UPDATED));
        return true;
    }

    public void delete(String uri){

        try{
            // delete from contains
            ResultSet result = dbSessionManager.getCommonSession().execute("select uri from contains where starturi='" + uri + "';");
            Iterator<Row> iterator = result.iterator();
            if (iterator != null){
                while(iterator.hasNext()){
                    String relUri = iterator.next().getString(0);
                    udm.delete(Relation.Type.CONTAINS_TO_ITEM).byUri(relUri);
                }
            }

            // delete from domains
            udm.delete(Resource.Type.DOMAIN).byUri(uri);

            // delete from resources_by_domain
            Arrays.stream(Resource.Type.values()).parallel().forEach(type -> execute("delete from " + TABLE_NAME + " where domain='" + uri +"' and type='"+type.key()+"';"));

            // delete counters
            counterDao.removeAll(uri);

        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
        }

    }

    public void removeItem(String domainUri, String itemUri){

        Optional<Row> row = super.oneQuery("select uri from contains where starturi='" + domainUri + "' and enduri='" + itemUri + "' ALLOW FILTERING;");

        if (!row.isPresent()) return;

        // Delete parts
        Integer windowSize = 100;
        Optional<String> offset = Optional.empty();
        Boolean completed = false;
        while(!completed){

            List<Part> parts = itemsDao.listParts(itemUri,windowSize,offset, false);

            for (Part part : parts){
                removePart(domainUri, part.getUri());
            }

            if (parts.size() < windowSize) break;

            offset = Optional.of(URIGenerator.retrieveId(parts.get(parts.size()-1).getUri()));
        }

        udm.delete(Relation.Type.CONTAINS_TO_ITEM).byUri(row.get().getString(0));

        // Remove from resources_by_domain
        delete(domainUri, itemUri);
        LOG.info("Deleted "+ itemUri + " from " + domainUri);

    }

    public boolean removePart(String domainUri, String partUri){
        return delete(domainUri, partUri);
    }

    public boolean removeSubdomain(String domainUri, String subdomainUri){
        Boolean result = delete(domainUri, subdomainUri);
        eventBus.post(Event.from(Relation.newContains(domainUri, subdomainUri)), RoutingKey.of("subdomain.deleted"));
        return result;
    }

    private Boolean delete(String domainUri, String resourceUri){
        Resource.Type type = URIGenerator.typeFrom(resourceUri);
        execute("delete from "+ TABLE_NAME + " where domain='"+ domainUri+"' and type='"+type.key()+"' and resource='" + resourceUri+"';");
        return counterDao.decrement(domainUri, type.route());
    }

    public void removeAllItems(String domainUri){

        // Remove from Contains
        Iterator<Row> it = super.iteratedQuery("select uri from contains where starturi='" + domainUri + "';");

        while(it.hasNext()){
            udm.delete(Relation.Type.CONTAINS_TO_ITEM).byUri(it.next().getString(0));
        }

        // Remove Items
        delete(domainUri, Resource.Type.ITEM);

        // Remove Parts
        removeAllParts(domainUri);

    }

    public void removeAllParts(String domainUri){
        delete(domainUri, Resource.Type.PART);
    }

    public void removeAllSubdomains(String domainUri){
        delete(domainUri, Resource.Type.DOMAIN);
    }

    public Boolean delete(String domainUri, Resource.Type type){
        if (execute("delete from " + TABLE_NAME + " where domain='" + domainUri+"' and type='" + type.key() + "';")){
            counterDao.reset(domainUri, type.route());
            return true;
        }
        return false;
    }

}
