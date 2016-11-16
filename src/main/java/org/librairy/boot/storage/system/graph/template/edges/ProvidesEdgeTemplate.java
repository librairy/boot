/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.template.edges;

import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ProvidesEdgeTemplate extends EdgeTemplate {

    public ProvidesEdgeTemplate() {
        super(Relation.Type.PROVIDES);
    }

    @Override
    protected String label() {
        return "PROVIDES";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Source)-[r:PROVIDES]->(e:Document)";
            case DOMAIN:        return "(d:Domain{uri:{0}})-[:CONTAINS]->(e:Document)<-[r:PROVIDES]-(s:Source)";
            case SOURCE:        return "(s:Source{uri:{0}})-[r:PROVIDES]->(e:Document)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case PROVIDES:      return "(s:Source{uri:{0}})-[r:PROVIDES]->(e:Document{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
