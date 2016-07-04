package org.librairy.storage.system.graph.template.edges;

import org.librairy.model.domain.relations.Relation;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class AppearedInEdgeTemplate extends EdgeTemplate {

    public AppearedInEdgeTemplate() {
        super(Relation.Type.APPEARED_IN);
    }

    @Override
    protected String label() {
        return "APPEARED_IN";
    }

    @Override
    protected String pathBy(org.librairy.model.domain.resources.Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Term)-[r:APPEARED_IN]->(e:Domain)";
            case DOMAIN:        return "(e:Domain{uri:{0}})<-[r:APPEARED_IN]-(s:Term)";
            case TERM:          return "(s:Term{uri:{0}})-[r:APPEARED_IN]->(e:Domain)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case APPEARED_IN:   return "(s:Term{uri:{0}})-[r:APPEARED_IN]->(e:Domain{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).
                add("consensus",relation.asAppearedIn().getConsensus()).
                add("cvalue",relation.asAppearedIn().getCvalue()).
                add("endType",relation.asAppearedIn().getEndType()).
                add("pertinence",relation.asAppearedIn().getPertinence()).
                add("probability",relation.asAppearedIn().getProbability()).
                add("subtermOf",relation.asAppearedIn().getSubtermOf()).
                add("supertermOf",relation.asAppearedIn().getSupertermOf()).
                add("termhood",relation.asAppearedIn().getTermhood()).
                add("times",relation.asAppearedIn().getTimes())
                ;
    }


}
