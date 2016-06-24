package org.librairy.storage.system.graph.template.nodes;

import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ItemNodeTemplate extends NodeTemplate {

    public ItemNodeTemplate() {
        super(Resource.Type.ITEM);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n:Item)<-[:BUNDLES]-(:Document)<-[:PROVIDES]-(source{uri:{0}})";
            case DOMAIN:    return "(n:Item)<-[:BUNDLES]-(:Document)<-[:CONTAINS]-(domain{uri:{0}})";
            case DOCUMENT:  return "(n:Item)<-[:BUNDLES]-(document{uri:{0}})";
            case ITEM:      return "(n:Item)<-[:SIMILAR_TO]-(item{uri:{0}})";
            case PART:      return "(n:Item)<-[:DESCRIBES]-(part{uri:{0}})";
            case TOPIC:     return "(n:Item)-[:DEALS_WITH]->(topic{uri:{0}})";
            case WORD:      return "(n:Item)-[:DEALS_WITH]->(:Topic)-[:MENTIONS]->(word{uri:{0}})";
            case TERM:      return "(n:Item)-[:DEALS_WITH]->(:Topic)-[:MENTIONS]->(:Word)<-[:MENTIONS]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }
}
