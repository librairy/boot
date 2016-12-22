/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.librairy.boot.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class DBSessionManager {
    @Autowired
    CassandraClusterFactoryBean clusterFactoryBean;

    ConcurrentHashMap<String,Session> sessions;

    private Cluster cluster;

    private Session controlSession;

    @PostConstruct
    public void setup(){
        this.cluster    = clusterFactoryBean.getObject();
        this.sessions   = new ConcurrentHashMap<>();
    }


    public Session getSessionByUri(String domainUri){
        return getSessionById(URIGenerator.retrieveId(domainUri));
    }

    public Session getSessionById(String id){
        String keyspaceId = getKeyspaceId(id);
        Session session = sessions.get(keyspaceId);

        if (session == null){
            session = this.cluster.connect(keyspaceId);
            sessions.put(keyspaceId,session);
        }

        return session;
    }

    public Session getSession(){
        if (controlSession == null){
            controlSession =  this.cluster.connect();
        }
        return controlSession;
    }

    public void closeSessionByUri(String uri){
        closeSessionById(URIGenerator.retrieveId(uri));
    }

    public void closeSessionById(String id){
        String keyspaceId = getKeyspaceId(id);
        if (sessions.contains(keyspaceId)){
            sessions.get(id).close();
            sessions.remove(keyspaceId);
        }
    }

    public void closeSession(){
        if (this.controlSession != null){
            this.controlSession.close();
        }
    }

    public static String getKeyspaceFromUri(String uri){
        return getKeyspaceId(URIGenerator.retrieveId(uri));
    }


    private static String getKeyspaceId(String domainId){
        //return (Character.isDigit(domainId.charAt(0)))? "d"+domainId : domainId;

        switch (domainId.toLowerCase()){
            case "research": return "research";
            case "default" : return "default";
            default: return "d"+domainId;
        }
    }
}
