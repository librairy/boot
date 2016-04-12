package org.librairy.storage.system.graph.domain;

import org.apache.commons.lang.StringUtils;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.neo4j.ogm.exception.CypherException;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Created on 12/04/16:
 *
 * @author cbadenes
 */
@Component
public class IndexCreator {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCreator.class);

    @Autowired
    Session session;

    Neo4jTemplate neo4jTemplate;

    @PostConstruct
    public void createIndexes() {

        this.neo4jTemplate = new Neo4jTemplate(session);

        Arrays.stream(Resource.Type.values()).forEach(type -> createIndex(type));

        // Version 2.3.1 seems to be no constraints on relations
//        Arrays.stream(Relation.Type.values()).forEach(type -> createIndex(type));

    }

    private void createIndex(Resource.Type type){
        try{
            String typeId = StringUtils.capitalize(type.name().toLowerCase());
            neo4jTemplate.execute("CREATE CONSTRAINT ON (node:" + typeId +") ASSERT node.uri IS UNIQUE");
            LOG.info("Index created for node: " + typeId);
        }catch (Exception e){
            if (e.getCause() instanceof CypherException){
                LOG.debug("Index probably already exists for: " + type, e);
            }else{
                LOG.warn("Error trying to define index for: " + type);
            }
        }
    }


    // Version 2.3.1 seems to be no constraints on relations
    private void createIndex(Relation.Type type){
        try{
            String typeId = type.name().toUpperCase();
            neo4jTemplate.execute("CREATE CONSTRAINT ON ()-[rel:" + typeId +"]->() ASSERT rel.uri IS UNIQUE");
            LOG.info("Index created for node: " + typeId);
        }catch (Exception e){
            if (e.getCause() instanceof CypherException){
                LOG.debug("Index probably already exists for: " + type, e);
            }else{
                LOG.warn("Error trying to define index for: " + type);
            }
        }
    }

}
