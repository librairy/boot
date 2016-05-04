package org.librairy.storage.system.graph.template.edges;

import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class SimilarDocEdgeTemplate extends EdgeTemplate {

    public SimilarDocEdgeTemplate() {
        super(org.librairy.model.domain.relations.Relation.Type.SIMILAR_TO_DOCUMENTS);
    }

    @Override
    protected String label() {
        return "SIMILAR_TO";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Document)-[r:SIMILAR_TO]->(e:Document)";
            case DOMAIN:        return "(d:Domain{uri:{0}})-[:CONTAINS]->(s:Document)-[r:SIMILAR_TO]->(e:Document)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(org.librairy.model.domain.relations.Relation.Type type) {
        switch (type){
            case SIMILAR_TO_DOCUMENTS:      return "(s:Document{uri:{0}})-[r:SIMILAR_TO]->(e:Document{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(org.librairy.model.domain.relations.Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToDocuments().getDomain());
    }
}
