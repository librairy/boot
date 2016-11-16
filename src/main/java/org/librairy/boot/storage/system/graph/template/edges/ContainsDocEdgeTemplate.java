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
public class ContainsDocEdgeTemplate extends EdgeTemplate {

    public ContainsDocEdgeTemplate() {
        super(Relation.Type.CONTAINS_TO_DOCUMENT);
    }

    @Override
    protected String label() {
        return "CONTAINS";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Domain)-[r:CONTAINS]->(e:Document)";
            case DOMAIN:        return "(s:Domain{uri:{0}})-[r:CONTAINS]->(e:Document)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case CONTAINS_TO_DOCUMENT:      return "(s:Domain{uri:{0}})-[r:CONTAINS]->(e:Document{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
