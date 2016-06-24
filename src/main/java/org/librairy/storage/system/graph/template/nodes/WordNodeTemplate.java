package org.librairy.storage.system.graph.template.nodes;

import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class WordNodeTemplate extends NodeTemplate {

    public WordNodeTemplate() {
        super(Resource.Type.WORD);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            case SOURCE:    return "(n:Word)-[:EMBEDDED_IN]->(:Domain)<-[:COMPOSES]-(source{uri:{0}})";
            case DOMAIN:    return "n where (:Domain{uri:{0}})<-[:EMBEDDED_IN]-(n:Word) or (:Domain{uri:{0}})" +
                    "<-[:EMERGES_IN]-(:Topic)-[:MENTIONS]->(n:Word) or (:Domain{uri:{0}})<-[:APPEARED_IN]-(:Term)" +
                    "-[:MENTIONS]->(n:Word)";
            case DOCUMENT:  return "(n:Word)<-[:MENTIONS]-(:Topic)<-[:DEALS_WITH]-(document{uri:{0}})";
            case ITEM:      return "(n:Word)<-[:MENTIONS]-(:Topic)<-[:DEALS_WITH]-(item{uri:{0}})";
            case PART:      return "(n:Word)<-[:MENTIONS]-(:Topic)<-[:DEALS_WITH]-(part{uri:{0}})";
            case TOPIC:     return "(n:Word)<-[:MENTIONS]-(topic{uri:{0}})";
            case WORD:      return "(n:Word)<-[:PAIRS_WITH]-(word{uri:{0}})";
            case TERM:      return "(n:Word)<-[:MENTIONS]-(term{uri:{0}})";
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }
}
