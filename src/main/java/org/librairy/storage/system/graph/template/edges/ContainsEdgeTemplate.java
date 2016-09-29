/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ContainsEdgeTemplate extends EdgeTemplate {

    public ContainsEdgeTemplate() {
        super(Relation.Type.CONTAINS);
    }

    @Override
    protected String label() {
        return "CONTAINS";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Domain)-[r:CONTAINS]->(e:Document)";
            case DOMAIN:        return "(s:Domain{uri:{0}})-[r:CONTAINS]->(e:Document)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case CONTAINS:      return "(s:Domain{uri:{0}})-[r:CONTAINS]->(e:Document{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
