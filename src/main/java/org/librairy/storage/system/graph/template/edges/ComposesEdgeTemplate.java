package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ComposesEdgeTemplate extends EdgeTemplate {

    public ComposesEdgeTemplate() {
        super(Relation.Type.COMPOSES);
    }

    @Override
    protected String label() {
        return "COMPOSES";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Source)-[r:COMPOSES]->(e:Domain)";
            case DOMAIN:        return "(e:Domain{uri:{0}})<-[r:COMPOSES]-(s:Source)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case COMPOSES:   return "(s:Source{uri:{0}})-[r:COMPOSES]->(e:Domain{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
