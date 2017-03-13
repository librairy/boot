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
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class DomainsDao extends AbstractDao  {

    private static final String DEFAULT_KEYSPACE = "research";

    private static final Logger LOG = LoggerFactory.getLogger(DomainsDao.class);

    @Autowired
    DBSessionManager dbSessionManager;

    @Autowired
    KeyspaceDao keyspaceDao;

    @Autowired
    UDM udm;

    @Autowired
    CounterDao counterDao;

    @Autowired
    ItemsDao itemsDao;

    public Boolean exists(String domainUri){
        String query = "select count(*) from contains where starturi='" + domainUri + "';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if ((row == null) || row.getLong(0) < 1) return false;

        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
        return true;
    }


    public Boolean contains(String domainUri, String resourceUri){
        String query = "select count(*) from contains where starturi='" + domainUri + "' and enduri='"+resourceUri+"' ALLOW FILTERING;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if ((row == null) || row.getLong(0) < 1) return false;

        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return false;
        }
        return true;
    }


    public Iterator<Row> listFrom(String resourceUri){
        String query = "select starturi from contains where enduri='"+resourceUri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Iterator<Row> iterator = result.iterator();

            if ((iterator == null)) return Collections.emptyIterator();

            return iterator;
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyIterator();
        }
    }

    public List<String> listOnly(String field){
        String query = "select "+field+" from domains;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream().map(row -> row.getString(0)).collect(Collectors.toList());
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }

    }

    public List<Domain> listAll(){
        String query = "select uri, name, description, creationtime from domains;";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            List<Row> rows = result.all();

            if ((rows == null) || rows.isEmpty()) return Collections.emptyList();

            return rows.stream().map(row -> {
                Domain domain = new Domain();
                domain.setUri(row.getString(0));
                domain.setName(row.getString(1));
                domain.setDescription(row.getString(2));
                domain.setCreationTime(row.getString(3));
                return domain;
            }).collect(Collectors.toList());
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Domain> list(Long size, String offset){


        StringBuilder query = new StringBuilder().append("select uri, name, creationtime, description from research.domains");

        if (offset != null){
            query.append(" where token(uri) >= token('" + URIGenerator.fromId(org.librairy.boot.model.domain.resources.Resource.Type
                            .ITEM,
                    offset) + "')");
        }

        query.append(" limit " + size);

        query.append(";");

        LOG.debug("Executing query: " + query);
        try{
            ResultSet result = dbSessionManager.getSession().execute(query.toString());
            Iterator<Row> it = result.iterator();

            List<Domain> resources = new ArrayList<>();

            while(it.hasNext()) {
                Row row = it.next();

                Domain domain = new Domain();
                domain.setUri(row.getString(0));
                domain.setName(row.getString(1));
                domain.setCreationTime(row.getString(2));
                domain.setDescription(row.getString(3));

                resources.add(domain);
            }

            return resources;
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
            return Collections.emptyList();
        }
    }


    public Domain get(String uri) throws DataNotFound {
        String query = "select name, description, creationtime from domains where uri='"+uri+"';";

        try{
            ResultSet result = dbSessionManager.getSessionById(DEFAULT_KEYSPACE).execute(query);
            Row row = result.one();

            if ((row == null)) throw new DataNotFound("No domain found by '" + uri + "'");

            Domain domain = new Domain();
            domain.setName(row.getString(0));
            domain.setDescription(row.getString(1));
            domain.setCreationTime(row.getString(2));
            domain.setUri(uri);

            return domain;
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            throw new DataNotFound("No domain found by '" + uri + "'");
        }
    }


    public Long count(String uri, String element) throws DataNotFound {
        String query = "select num from counts where name='" + element + "' ALLOW FILTERING;";

        try{
            ResultSet result = dbSessionManager.getSessionByUri(uri).execute(query);
            Row row = result.one();

            if ((row == null)) return 0l;

            return row.getLong(0);
        } catch (InvalidQueryException e){
            LOG.warn("Error on query: " + e.getMessage());
            throw new DataNotFound("No domain found by '" + uri + "'");
        }
    }


    public void delete(String uri){

        try{
            udm.delete(org.librairy.boot.model.domain.resources.Resource.Type.DOMAIN).byUri(uri);
            ResultSet result = dbSessionManager.getSession().execute("select uri from research.contains where starturi='" + uri + "';");

            Iterator<Row> iterator = result.iterator();
            while(iterator.hasNext()){
                String relUri = iterator.next().getString(0);
                dbSessionManager.getSession().execute("delete from research.contains where uri='"+relUri+"';");
            }
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution: " + e.getMessage());
        }

    }

    public void remove(String domainUri, String resourceUri){

        String query = "select uri from research.contains where starturi='" + domainUri + "' and enduri='" + resourceUri + "' ALLOW FILTERING;";
        try{
            ResultSet result = dbSessionManager.getSession().execute(query);

            Row row = result.one();

            if (row != null){
                dbSessionManager.getSession().execute("delete from research.contains where uri='" + row.getString(0) + "';");

                // decrement counter
                Resource.Type type = URIGenerator.typeFrom(resourceUri);

                counterDao.decrement(domainUri, type.route());

                if (type.equals(Resource.Type.ITEM)){

                    // Delete parts
                    Optional<String> offset = Optional.empty();
                    Boolean completed = false;
                    while(!completed){

                        List<String> parts = null;
                        parts = itemsDao.listParts(resourceUri,100,offset);

                        for (String uri : parts){
                            remove(domainUri, uri);
                        }

                        if (parts.isEmpty() || parts.size() < 100) break;

                        String lastUri = parts.get(parts.size()-1);
                        offset = Optional.of(URIGenerator.retrieveId(lastUri));
                    }

                }


            }
            LOG.info("Deleted "+ resourceUri + " from " + domainUri);
        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
        }
    }

    public void removeAllDocuments(String domainUri){
        String query = "select uri from research.contains where starturi='" + domainUri +"';";
        try{

            ResultSet result = dbSessionManager.getSession().execute(query);
            Iterator<Row> it = result.iterator();

            while(it.hasNext()){
                dbSessionManager.getSession().execute("delete from research.contains where " + "uri='" + it.next().getString(0) + "';");
            }

            dbSessionManager.getSessionByUri(domainUri).execute("drop table if exists items;");
            dbSessionManager.getSessionByUri(domainUri).execute("drop table if exists parts;");

            counterDao.reset(domainUri, Resource.Type.ITEM.route());
            counterDao.reset(domainUri, Resource.Type.PART.route());

        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
        }
    }

    public void removeAllParts(String domainUri){
        String query = "select uri, enduri from research.contains where starturi='" + domainUri +"';";
        try{

            ResultSet result = dbSessionManager.getSession().execute(query);
            Iterator<Row> it = result.iterator();

            while(it.hasNext()){
                Row row = it.next();
                String uri      = row.getString(0);
                String resUri   = row.getString(1);
                if (URIGenerator.typeFrom(resUri).equals(Resource.Type.PART)){
                    dbSessionManager.getSession().execute("delete from research.contains where " + "uri='" + uri + "';");
                }
            }

            dbSessionManager.getSessionByUri(domainUri).execute("drop table if exists parts;");

            counterDao.reset(domainUri, Resource.Type.PART.route());

        }catch (InvalidQueryException e){
            LOG.warn("Error on query execution [" + query + "] : " + e.getMessage());
        }
    }


}
