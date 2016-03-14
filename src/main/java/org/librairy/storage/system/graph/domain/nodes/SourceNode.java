package org.librairy.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.storage.system.graph.domain.edges.ComposesEdge;
import org.librairy.storage.system.graph.domain.edges.Edge;
import org.librairy.storage.system.graph.domain.edges.ProvidesEdge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Source")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class SourceNode extends Node {

    @Relationship(type = "COMPOSES", direction="OUTGOING")
    private Set<ComposesEdge> domains =  new HashSet<>();

    @Relationship(type = "PROVIDES", direction="OUTGOING")
    private Set<ProvidesEdge> documents =  new HashSet<>();

    @Override
    public void add(Edge edge) {
        switch(edge.getType()){
            case COMPOSES:
                domains.add((ComposesEdge) edge);
                break;
            case PROVIDES:
                documents.add((ProvidesEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Source Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch(edge.getType()){
            case COMPOSES:
                domains.remove((ComposesEdge) edge);
                break;
            case PROVIDES:
                documents.remove((ProvidesEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Source Node");
        }
    }

}
