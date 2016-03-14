package org.librairy.storage.system.graph.template;

import org.librairy.model.domain.relations.Relation;
import org.librairy.storage.system.graph.template.edges.EdgeTemplate;
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

    Map<Relation.Type, EdgeTemplate> edgeTable;

    @PostConstruct
    public void setup(){
        edgeTable = new HashMap<>();
        for (EdgeTemplate edgeTemplate : edgeTemplates){
            edgeTable.put(edgeTemplate.accept(), edgeTemplate);
        }
    }

    public EdgeTemplate of(Relation.Type type){
        return edgeTable.get(type);
    }

    public boolean handle(Relation.Type type){
        return edgeTable.containsKey(type);
    }
}
