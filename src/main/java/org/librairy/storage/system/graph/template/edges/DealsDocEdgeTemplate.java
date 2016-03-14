package org.librairy.storage.system.graph.template.edges;

import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsDocEdgeTemplate extends EdgeTemplate {

    public DealsDocEdgeTemplate() {
        super(org.librairy.model.domain.relations.Relation.Type.DEALS_WITH_FROM_DOCUMENT);
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Document)-[r:DEALS_WITH]->(e:Topic)";
            case DOMAIN:        return "(domain:Domain{uri:{0}})-[c:CONTAINS]->(s:Document)-[r:DEALS_WITH]->(e:Topic)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(org.librairy.model.domain.relations.Relation.Type type) {
        switch (type){
            case DEALS_WITH_FROM_DOCUMENT:      return "(s:Document{uri:{0}})-[r:DEALS_WITH]->(e:Topic{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(org.librairy.model.domain.relations.Relation relation) {
        return new TemplateParameters(relation);
    }
}
