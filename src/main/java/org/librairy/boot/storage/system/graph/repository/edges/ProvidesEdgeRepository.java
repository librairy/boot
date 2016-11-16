/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.edges;

import org.librairy.boot.storage.system.graph.domain.edges.ProvidesEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ProvidesEdgeRepository extends RelationGraphRepository<ProvidesEdge> {

    // To avoid a class type exception
    @Override
    ProvidesEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:PROVIDES]->(node2{uri:{1}}) return r")
    Iterable<ProvidesEdge> findByNodes(String start, String end);

    @Query("match (:Source)-[r:PROVIDES]->(:Document)<-[:CONTAINS]-(domain{uri:{0}}) return r")
    Iterable<ProvidesEdge> findByDomain(String uri);

    @Query("match (source{uri:{0}})-[r:PROVIDES]->(document) return r")
    Iterable<ProvidesEdge> findBySource(String uri);

    @Query("match (source)-[r:PROVIDES]->(document{uri:{0}}) return r")
    Iterable<ProvidesEdge> findByDocument(String uri);
}
