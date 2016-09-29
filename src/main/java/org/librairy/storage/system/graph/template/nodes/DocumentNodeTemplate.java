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
public class DocumentNodeTemplate extends NodeTemplate {

    public DocumentNodeTemplate() {
        super(Resource.Type.DOCUMENT);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n:Document)<-[:PROVIDES]-(source{uri:{0}})";
            case DOMAIN:    return "(n:Document)<-[:CONTAINS]-(domain{uri:{0}})";
            case DOCUMENT:  return "(n:Document)-[:SIMILAR_TO]-(document{uri:{0}})";
            case ITEM:      return "(n:Document)-[:BUNDLES]->(item{uri:{0}})";
            case PART:      return "(n:Document)-[:BUNDLES]->(:Item)<-[:DESCRIBES]-(part{uri:{0}})";
            case TOPIC:     return "(n:Document)-[:DEALS_WITH]->(topic{uri:{0}})";
            case WORD:      return "(n:Document)-[:DEALS_WITH]->(:Topic)-[:MENTIONS]->(word{uri:{0}})";
            case TERM:      return "(n:Document)-[:DEALS_WITH]->(:Topic)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }

}
