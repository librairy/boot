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
import org.librairy.storage.system.graph.domain.edges.DealsWithFromPartEdge;
import org.librairy.storage.system.graph.domain.edges.DescribesEdge;
import org.librairy.storage.system.graph.domain.edges.Edge;
import org.librairy.storage.system.graph.domain.edges.SimilarToPartsEdge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Part")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class PartNode extends Node {

    private String sense;

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarToPartsEdge> parts = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<DealsWithFromPartEdge> topics = new HashSet<>();
    
    @Relationship(type = "DESCRIBES", direction="OUTGOING")
    private Set<DescribesEdge> items = new HashSet<>();


    @Override
    public void add(Edge edge) {
        switch(edge.getType()){
            case DEALS_WITH_FROM_PART:
                topics.add((DealsWithFromPartEdge) edge);
                break;
            case DESCRIBES:
                items.add((DescribesEdge) edge);
                break;
            case SIMILAR_TO_PARTS:
                parts.add((SimilarToPartsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Part Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch(edge.getType()){
            case DEALS_WITH_FROM_PART:
                topics.remove(edge);
                break;
            case DESCRIBES:
                items.remove(edge);
                break;
            case SIMILAR_TO_PARTS:
                parts.remove(edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Part Node");
        }
    }
}
