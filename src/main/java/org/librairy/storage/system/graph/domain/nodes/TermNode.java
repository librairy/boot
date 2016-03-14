package org.librairy.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.storage.system.graph.domain.edges.Edge;
import org.librairy.storage.system.graph.domain.edges.EmbeddedInEdge;
import org.librairy.storage.system.graph.domain.edges.PairsWithEdge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Term")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class TermNode extends Node {

    private String content;

    @Relationship(type = "PAIRS_WITH", direction="UNDIRECTED")
    private Set<PairsWithEdge> words = new HashSet<>();

    @Relationship(type = "EMBEDDED_IN", direction="OUTGOING")
    private Set<EmbeddedInEdge> domains = new HashSet<>();

    @Override
    public void add(Edge edge) {
        switch(edge.getType()){
            case EMBEDDED_IN:
                domains.add((EmbeddedInEdge) edge);
                break;
            case PAIRS_WITH:
                words.add((PairsWithEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Word Node to be added");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch(edge.getType()){
            case EMBEDDED_IN:
                domains.remove((EmbeddedInEdge) edge);
                break;
            case PAIRS_WITH:
                words.remove((PairsWithEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Word Node to be removed");
        }
    }
}
