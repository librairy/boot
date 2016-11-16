/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.boot.storage.system.graph.domain.edges.Edge;
import org.librairy.boot.storage.system.graph.domain.edges.DealsWithFromItemEdge;
import org.librairy.boot.storage.system.graph.domain.edges.SimilarToItemsEdge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Item")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class ItemNode extends Node {

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarToItemsEdge> items = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<DealsWithFromItemEdge> topics =  new HashSet<>();


    @Override
    public void add(Edge edge) {
        switch (edge.getType()){
            case DEALS_WITH_FROM_ITEM:
                topics.add((DealsWithFromItemEdge) edge);
                break;
            case SIMILAR_TO_ITEMS:
                items.add((SimilarToItemsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Item Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch (edge.getType()){
            case DEALS_WITH_FROM_ITEM:
                topics.remove((DealsWithFromItemEdge) edge);
                break;
            case SIMILAR_TO_ITEMS:
                items.remove((SimilarToItemsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Item Node");
        }
    }
}
