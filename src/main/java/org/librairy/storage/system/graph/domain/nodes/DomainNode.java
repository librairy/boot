package org.librairy.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.storage.system.graph.domain.edges.ContainsEdge;
import org.librairy.storage.system.graph.domain.edges.Edge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Domain")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class DomainNode extends Node {

    @Relationship(type = "CONTAINS", direction="OUTGOING")
    private Set<ContainsEdge> documents = new HashSet<>();

    @Override
    public void add(Edge edge) {
        switch(edge.getType()){
            case CONTAINS:
                documents.add((ContainsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Domain Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch(edge.getType()){
            case CONTAINS:
                documents.remove((ContainsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Domain Node");
        }
    }
}
