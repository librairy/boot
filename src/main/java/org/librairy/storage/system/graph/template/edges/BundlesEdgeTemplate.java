package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class BundlesEdgeTemplate extends EdgeTemplate {

    public BundlesEdgeTemplate() {
        super(Relation.Type.BUNDLES);
    }

    @Override
    protected String label() {
        return "BUNDLES";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Document)-[r:BUNDLES]->(e:Item)";
            case DOMAIN:        return "(d:Domain{uri:{0}})-[:CONTAINS]->(s:Document)-[r:BUNDLES]->(e:Item)";
            case DOCUMENT:      return "(s:Document{uri:{0}})-[r:BUNDLES]->(e:Item)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case BUNDLES:   return "(s:Document{uri:{0}})-[r:BUNDLES]->(e:Item{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }


    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation);
    }


}
