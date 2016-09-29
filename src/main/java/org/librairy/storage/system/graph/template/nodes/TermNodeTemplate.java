/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.template.nodes;

import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class TermNodeTemplate extends NodeTemplate {

    public TermNodeTemplate() {
        super(Resource.Type.TERM);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n:Term)-[:APPEARED_IN]->(:Domain)<-[:COMPOSES]-(source{uri:{0}})";
            case DOMAIN:    return "(n:Term)-[:APPEARED_IN]->(domain{uri:{0}})";
            case DOCUMENT:  return "(n:Term)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(:Topic)<-[:DEALS_WITH]-" +
                    "(document{uri:{0}})";
            case ITEM:      return "(n:Term)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(:Topic)<-[:DEALS_WITH]-" +
                    "(item{uri:{0}})";
            case PART:      return "(n:Term)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(:Topic)<-[:DEALS_WITH]-" +
                    "(part{uri:{0}})";
            case TOPIC:     return "(n:Term)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(topic{uri:{0}})";
            case WORD:      return "(n:Term)-[:MENTIONS]->(word{uri:{0}})";
            case TERM:      return "(n:Term)<-[:HYPERNYM_OF]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }

}
