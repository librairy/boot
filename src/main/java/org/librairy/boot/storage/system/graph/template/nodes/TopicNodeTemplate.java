/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.template.nodes;

import org.librairy.boot.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class TopicNodeTemplate extends NodeTemplate {

    public TopicNodeTemplate() {
        super(Resource.Type.TOPIC);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n:Topic)-[:EMERGES_IN]->(:Domain)<-[:COMPOSES]-(source{uri:{0}})";
            case DOMAIN:    return "(n:Topic)-[:EMERGES_IN]->(domain{uri:{0}})";
            case DOCUMENT:  return "(n:Topic)<-[:DEALS_WITH]-(document{uri:{0}})";
            case ITEM:      return "(n:Topic)<-[:DEALS_WITH]-(item{uri:{0}})";
            case PART:      return "(n:Topic)<-[:DEALS_WITH]-(part{uri:{0}})";
            case TOPIC:     return "(n{uri:{0}})";
            case WORD:      return "(n:Topic)-[:MENTIONS]->(word{uri:{0}})";
            case TERM:      return "(n:Topic)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }
}
