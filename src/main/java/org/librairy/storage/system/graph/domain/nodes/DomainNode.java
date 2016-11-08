/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.storage.system.graph.domain.edges.ContainsToDocumentEdge;
import org.librairy.storage.system.graph.domain.edges.ContainsToItemEdge;
import org.librairy.storage.system.graph.domain.edges.ContainsToPartEdge;
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
    private Set<ContainsToDocumentEdge> documents = new HashSet<>();

    @Relationship(type = "CONTAINS", direction="OUTGOING")
    private Set<ContainsToItemEdge> items = new HashSet<>();

    @Relationship(type = "CONTAINS", direction="OUTGOING")
    private Set<ContainsToPartEdge> parts = new HashSet<>();

    @Override
    public void add(Edge edge) {
        switch(edge.getType()){
            case CONTAINS_TO_DOCUMENT:
                documents.add((ContainsToDocumentEdge) edge);
                break;
            case CONTAINS_TO_ITEM:
                items.add((ContainsToItemEdge) edge);
                break;
            case CONTAINS_TO_PART:
                parts.add((ContainsToPartEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Domain Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch(edge.getType()){
            case CONTAINS_TO_DOCUMENT:
                documents.remove((ContainsToDocumentEdge) edge);
                break;
            case CONTAINS_TO_ITEM:
                items.remove((ContainsToItemEdge) edge);
                break;
            case CONTAINS_TO_PART:
                parts.remove((ContainsToPartEdge)edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Domain Node");
        }
    }
}
