/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.edges;

import org.librairy.boot.storage.system.graph.domain.edges.HypernymOfEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface HypernymOfEdgeRepository extends RelationGraphRepository<HypernymOfEdge> {

    // To avoid a class type exception
    @Override
    HypernymOfEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:PAIRS_WITH]->(node2{uri:{1}}) return r")
    Iterable<HypernymOfEdge> findByNodes(String start, String end);

    @Query("match (:Term)<-[r:HYPERNYM_OF{domain:{0}}]-(:Term) return r")
    Iterable<HypernymOfEdge> findByDomain(String uri);

    @Query("match (term{uri:{0}})-[r:HYPERNYM_OF]-(t) return r")
    Iterable<HypernymOfEdge> findByTerm(String uri);

}
