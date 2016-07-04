package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class MentionsTermEdgeTemplate extends EdgeTemplate {

    public MentionsTermEdgeTemplate() {
        super(Relation.Type.MENTIONS_FROM_TERM);
    }

    @Override
    protected String label() {
        return "MENTIONS";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Term)-[r:MENTIONS]->(e:Word)";
            case DOMAIN:        return "(d:Domain{uri:{0}})<-[:APPEARED_IN]-(s:Term)-[r:MENTIONS]->(e:Word)";
            case TERM:          return "(s:Term{uri:{0}})-[r:MENTIONS]->(e:Word)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case MENTIONS_FROM_TERM:      return "(s:Term{uri:{0}})-[r:MENTIONS]->(e:Word{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("times",relation.asMentionsFromTerm().getTimes());
    }

}
