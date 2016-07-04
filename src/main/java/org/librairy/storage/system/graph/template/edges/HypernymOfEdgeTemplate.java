package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class HypernymOfEdgeTemplate extends EdgeTemplate {

    public HypernymOfEdgeTemplate() {
        super(Relation.Type.HYPERNYM_OF);
    }

    @Override
    protected String label() {
        return "HYPERNYM_OF";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Term)-[r:HYPERNYM_OF]->(e:Term)";
            case DOMAIN:        return "(dom1:Domain{uri:{0}})<-[app1:APPEARED_IN]-(e:Term)-[r:HYPERNYM_OF]->(s:Term)";
            case TERM:          return "(s:Term{uri:{0}})-[r:HYPERNYM_OF]->(e:Term)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case HYPERNYM_OF:      return "(s:Term{uri:{0}})-[r:HYPERNYM_OF]->(e:Term{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).
                add("domain",relation.asHypernymOf().getDomain());
    }


}
