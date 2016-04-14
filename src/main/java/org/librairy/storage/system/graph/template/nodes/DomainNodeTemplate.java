package org.librairy.storage.system.graph.template.nodes;

import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DomainNodeTemplate extends NodeTemplate {

    public DomainNodeTemplate() {
        super(Resource.Type.DOMAIN);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n:Domain)<-[:COMPOSES]-(source{uri:{0}})";
            case DOMAIN:    return "(n{uri:{0}})";
            case DOCUMENT:  return "(n:Domain)-[:CONTAINS]->(document{uri:{0}})";
            case ITEM:      return "(n:Domain)-[:CONTAINS]->(:Document)-[:BUNDLES]->(item{uri:{0}})";
            case PART:      return "(n:Domain)-[:CONTAINS]->(:Document)-[:BUNDLES]->(:Item)<-[:DESCRIBES]-(part{uri:{0}})";
            case TOPIC:     return "(n:Domain)<-[:EMERGES_IN]-(topic{uri:{0}})";
            case WORD:      return "(n:Domain)<-[:EMBEDDED_IN]-(word{uri:{0}})";
            case TERM:      return "(n:Domain)<-[:APPEARED_IN]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }

}
