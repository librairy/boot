/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.edges;

import org.librairy.boot.storage.system.graph.domain.edges.ContainsToItemEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ContainsToItemEdgeRepository extends RelationGraphRepository<ContainsToItemEdge> {

    // To avoid a class type exception
    @Override
    ContainsToItemEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:CONTAINS]->(node2{uri:{1}}) return r")
    Iterable<ContainsToItemEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[r:CONTAINS]->(:Item) return r")
    Iterable<ContainsToItemEdge> findByDomain(String uri);

    @Query("match (domain)-[r:CONTAINS]->(item{uri:{0}}) return r")
    Iterable<ContainsToItemEdge> findByItem(String uri);

}
