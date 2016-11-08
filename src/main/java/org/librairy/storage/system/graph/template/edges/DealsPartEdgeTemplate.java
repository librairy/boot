/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.template.edges;

import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsPartEdgeTemplate extends EdgeTemplate {

    public DealsPartEdgeTemplate() {
        super(org.librairy.model.domain.relations.Relation.Type.DEALS_WITH_FROM_PART);
    }

    @Override
    protected String label() {
        return "DEALS_WITH";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Part)-[r:DEALS_WITH]->(e:Topic)";
            case DOMAIN:        return "(domain:Domain{uri:{0}})-[c:CONTAINS]->(s:Part)-[r:DEALS_WITH]->(e:Topic)";
            case TOPIC:         return "(e:Topic{uri:{0}})<-[r:DEAL_WITH]-(s:Part)";
            case PART:          return "(e:Topic)<-[r:DEALS_WITH]-(s:Part{uri:{0}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(org.librairy.model.domain.relations.Relation.Type type) {
        switch (type){
            case DEALS_WITH_FROM_PART:      return "(s:Part{uri:{0}})-[r:DEALS_WITH]->(e:Topic{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(org.librairy.model.domain.relations.Relation relation) {
        return new TemplateParameters(relation);
    }

}
