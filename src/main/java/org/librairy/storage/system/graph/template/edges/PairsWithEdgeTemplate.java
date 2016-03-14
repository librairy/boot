package org.librairy.storage.system.graph.template.edges;

import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class PairsWithEdgeTemplate extends EdgeTemplate {

    public PairsWithEdgeTemplate() {
        super(org.librairy.model.domain.relations.Relation.Type.PAIRS_WITH);
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Word)-[r:PAIRS_WITH]->(e:Word)";
            case DOMAIN:        return "(d:Domain{uri:{0}})<-[:EMBEDDED_IN]-(s:Word)-[r:PAIRS_WITH]->(e:Word)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(org.librairy.model.domain.relations.Relation.Type type) {
        switch (type){
            case PAIRS_WITH:      return "(s:Word{uri:{0}})-[r:PAIRS_WITH]->(e:Word{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(org.librairy.model.domain.relations.Relation relation) {
        return new TemplateParameters(relation);
    }

}
