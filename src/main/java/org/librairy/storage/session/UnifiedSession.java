package org.librairy.storage.session;

import org.librairy.storage.actions.RepeatableActionExecutor;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedSession extends RepeatableActionExecutor{

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedSession.class);

    @Value("${librairy.neo4j.contactpoints}")
    String neo4jHost;

    @Value("${librairy.neo4j.port}")
    String neo4jPort;

    @Autowired
    SessionFactory neo4jSessionFactory;

    @Autowired
    Session neo4jSession;

    @PostConstruct
    public void setup(){
//        this.neo4jSession = (Neo4jSession) neo4jSessionFactory.openSession();
    }

    public void clean(){

//        this.neo4jSession.clear();
    }


    public UnifiedTransaction beginTransaction(){
        UnifiedTransaction transaction = new UnifiedTransaction();

//////        //TODO bug on neo4j-ogm library: check updates from 1.1.5
//        Optional<Object> result = performRetries(0, "getting neo4j session", () -> this.neo4jSession.beginTransaction());
//
//        if (result.isPresent()){
//            transaction.setNeo4jTransaction((Transaction) result.get());
//        }else{
//            LOG.warn("no transaction created for the next operation");
//        }
//
//        //TODO Add Cassandra Transaction
//        //TODO Add Elasticsearch Transaction
        return transaction;
    }

}
