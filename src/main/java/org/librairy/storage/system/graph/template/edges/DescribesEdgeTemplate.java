package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DescribesEdgeTemplate extends EdgeTemplate {

    public DescribesEdgeTemplate() {
        super(Relation.Type.DESCRIBES);
    }

    @Override
    protected String label() {
        return "DESCRIBES";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Part)-[r:DESCRIBES]->(e:Item)";
            case DOMAIN:        return "(d:Domain{uri:{0}})-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(e:Item)<-[r:DESCRIBES]-(s:Part)";
            case PART:          return "(s:Part{uri:{0}})-[r:DESCRIBES]->(e:Item)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case DESCRIBES:      return "(s:Part{uri:{0}})-[r:DESCRIBES]->(e:Item{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
