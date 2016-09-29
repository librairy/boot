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
import org.librairy.storage.system.graph.domain.edges.BundlesEdge;
import org.librairy.storage.system.graph.domain.edges.DealsWithFromDocumentEdge;
import org.librairy.storage.system.graph.domain.edges.Edge;
import org.librairy.storage.system.graph.domain.edges.SimilarToDocumentsEdge;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Document")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class DocumentNode extends Node {

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarToDocumentsEdge> documents = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<DealsWithFromDocumentEdge> topics = new HashSet<>();

    @Relationship(type = "BUNDLES", direction="OUTGOING")
    private Set<BundlesEdge> items = new HashSet<>();

    @Override
    public void add(Edge edge) {
        switch (edge.getType()){
            case BUNDLES:
                items.add((BundlesEdge) edge);
                break;
            case DEALS_WITH_FROM_DOCUMENT:
                topics.add((DealsWithFromDocumentEdge) edge);
                break;
            case SIMILAR_TO_DOCUMENTS:
                documents.add((SimilarToDocumentsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Document Node");
        }
    }

    @Override
    public void remove(Edge edge) {
        switch (edge.getType()){
            case BUNDLES:
                items.remove((BundlesEdge) edge);
                break;
            case DEALS_WITH_FROM_DOCUMENT:
                topics.remove((DealsWithFromDocumentEdge) edge);
                break;
            case SIMILAR_TO_DOCUMENTS:
                documents.remove((SimilarToDocumentsEdge) edge);
                break;
            default: throw new RuntimeException( "Relation " + edge + " not handled from Document Node");
        }
    }
}
