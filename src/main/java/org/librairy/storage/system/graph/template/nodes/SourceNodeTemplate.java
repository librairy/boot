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
public class SourceNodeTemplate extends NodeTemplate {

    public SourceNodeTemplate() {
        super(Resource.Type.SOURCE);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n{uri:{0}})";
            case DOMAIN:    return "(n:Source)-[:COMPOSES]->(domain{uri:{0}})";
            case DOCUMENT:  return "(n:Source)-[:PROVIDES]->(document{uri:{0}})";
            case ITEM:      return "(n:Source)-[:PROVIDES]->(:Document)-[:BUNDLES]->(item{uri:{0}})";
            case PART:      return "(n:Source)-[:PROVIDES]->(:Document)-[:BUNDLES]->(:Item)<-[:DESCRIBES]-(part{uri:{0}})";
            case TOPIC:     return "(n:Source)-[:COMPOSES]->(:Domain)<-[:EMERGES_IN]-(topic{uri:{0}})";
            case WORD:      return "(n:Source)-[:COMPOSES]->(:Domain)<-[:EMBEDDED_IN]-(word{uri:{0}})";
            case TERM:      return "(n:Source)-[:COMPOSES]->(:Domain)<-[:APPEARED_IN]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }
}
