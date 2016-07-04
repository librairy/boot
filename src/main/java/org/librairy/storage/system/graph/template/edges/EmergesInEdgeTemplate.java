package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class EmergesInEdgeTemplate extends EdgeTemplate {

    public EmergesInEdgeTemplate() {
        super(Relation.Type.EMERGES_IN);
    }

    @Override
    protected String label() {
        return "EMERGES_IN";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Topic)-[r:EMERGES_IN]->(e:Domain)";
            case DOMAIN:        return "(e:Domain{uri:{0}})<-[r:EMERGES_IN]-(s:Topic)";
            case TOPIC:         return "(e:Domain)<-[r:EMERGES_IN]-(s:Topic{uri:{0}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case EMERGES_IN:      return "(s:Topic{uri:{0}})-[r:EMERGES_IN]->(e:Domain{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("analysis",relation.asEmergesIn().getAnalysis());
    }


}
