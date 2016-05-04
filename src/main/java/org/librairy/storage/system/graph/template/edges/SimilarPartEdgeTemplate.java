package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class SimilarPartEdgeTemplate extends EdgeTemplate {

    public SimilarPartEdgeTemplate() {
        super(Relation.Type.SIMILAR_TO_PARTS);
    }

    @Override
    protected String label() {
        return "SIMILAR_TO";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Part)-[r:SIMILAR_TO]->(e:Part)";
            case DOMAIN:        return "(dom:Domain{uri:{0}})-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(i:Item)<-[:DESCRIBES]-(s:Part)-[r:SIMILAR_TO]->(e:Part)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case SIMILAR_TO_PARTS:      return "(s:Part{uri:{0}})-[r:SIMILAR_TO]->(e:Part{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToParts().getDomain());
    }

}
