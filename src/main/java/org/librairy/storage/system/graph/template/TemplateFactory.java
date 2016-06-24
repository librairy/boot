package org.librairy.storage.system.graph.template;

import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.system.graph.template.edges.EdgeTemplate;
import org.librairy.storage.system.graph.template.nodes.NodeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class TemplateFactory {

    @Autowired
    List<EdgeTemplate> edgeTemplates;

    @Autowired
    List<NodeTemplate> nodeTemplates;

    Map<Relation.Type, EdgeTemplate> edgeTable;

    Map<Resource.Type, NodeTemplate> nodeTable;

    @PostConstruct
    public void setup(){
        edgeTable = new HashMap<>();
        for (EdgeTemplate edgeTemplate : edgeTemplates){
            edgeTable.put(edgeTemplate.accept(), edgeTemplate);
        }

        nodeTable = new HashMap<>();
        for (NodeTemplate nodeTemplate : nodeTemplates){
            nodeTable.put(nodeTemplate.accept(), nodeTemplate);
        }
    }

    public EdgeTemplate of(Relation.Type type){
        return edgeTable.get(type);
    }

    public boolean handle(Relation.Type type){
        return edgeTable.containsKey(type);
    }

    public NodeTemplate of(Resource.Type type){
        return nodeTable.get(type);
    }

    public boolean handle(Resource.Type type){
        return nodeTable.containsKey(type);
    }
}
