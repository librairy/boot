/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.ContainsToItemEdge;
import org.librairy.storage.system.graph.domain.edges.ContainsToPartEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ContainsToPartEdgeRepository extends RelationGraphRepository<ContainsToPartEdge> {

    // To avoid a class type exception
    @Override
    ContainsToPartEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:CONTAINS]->(node2{uri:{1}}) return r")
    Iterable<ContainsToPartEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[r:CONTAINS]->(:Part) return r")
    Iterable<ContainsToPartEdge> findByDomain(String uri);

    @Query("match (domain)-[r:CONTAINS]->(part{uri:{0}}) return r")
    Iterable<ContainsToPartEdge> findByPart(String uri);

}
