/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.BundlesEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface BundlesEdgeRepository extends RelationGraphRepository<BundlesEdge> {

    // To avoid a class type exception
    @Override
    BundlesEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:BUNDLES]->(node2{uri:{1}}) return r")
    Iterable<BundlesEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Document)-[r:BUNDLES]->(:Item) return r")
    Iterable<BundlesEdge> findByDomain(String uri);

    @Query("match (document{uri:{0}})-[r:BUNDLES]->(:Item) return r")
    Iterable<BundlesEdge> findByDocument(String uri);

    @Query("match (item{uri:{0}})<-[r:BUNDLES]-(document) return r")
    Iterable<BundlesEdge> findByItem(String uri);

}
