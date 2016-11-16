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
public class DealsItemEdgeTemplate extends EdgeTemplate {

    public DealsItemEdgeTemplate() {
        super(Relation.Type.DEALS_WITH_FROM_ITEM);
    }

    @Override
    protected String label() {
        return "DEALS_WITH";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Item)-[r:DEALS_WITH]->(e:Topic)";
            case DOMAIN:        return "(domain:Domain{uri:{0}})-[c:CONTAINS]->(s:Item)-[r:DEALS_WITH]->(e:Topic)";
            case TOPIC:         return "(e:Topic{uri:{0}})<-[r:DEALS_WITH]-(s:Item)";
            case ITEM:          return "(e:Topic)<-[r:DEALS_WITH]-(s:Item{uri:{0}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case DEALS_WITH_FROM_ITEM:      return "(s:Item{uri:{0}})-[r:DEALS_WITH]->(e:Topic{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }
}
