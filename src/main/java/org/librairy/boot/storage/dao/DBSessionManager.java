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
        return getDomainSession(URIGenerator.retrieveId(domainUri));
    }


    public Session getCommonSession(){
        return getSession(getCommonKeyspaceId());
    }

    public Session getDomainSession(String domainId){
        return getSession(getDomainKeyspaceId(domainId));
    }

    public Session getSpecificSession(String scope, String domainId){
        return getSession(getSpecificKeyspaceId(scope, domainId));
    }

    private Session getSession(String keyspaceId){
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
        closeDomainSessionById(URIGenerator.retrieveId(uri));
    }

    public void closeDomainSessionById(String id){
        closeSession(getDomainKeyspaceId(id));
    }

    public void closeSpecificSessionById(String scope, String id){
        closeSession(getSpecificKeyspaceId(scope,id));
    }


    public void closeCommonsSession(){
        closeSession(getCommonKeyspaceId());
        if (this.controlSession != null){
            this.controlSession.close();
        }
    }

    private void closeSession(String id){
        if (sessions.contains(id)){
            sessions.get(id).close();
            sessions.remove(id);
        }
    }

    public static String getKeyspaceFromUri(String uri){
        return getDomainKeyspaceId(URIGenerator.retrieveId(uri));
    }


    public static String getCommonKeyspaceId(){
        return "research";
    }

    public static String getDomainKeyspaceId(String domainId){
        return "d" + domainId.toLowerCase();
    }

    public static String getSpecificKeyspaceId(String scope,String domainId){
        return scope.toLowerCase() +"_" + domainId.toLowerCase();
    }
}
