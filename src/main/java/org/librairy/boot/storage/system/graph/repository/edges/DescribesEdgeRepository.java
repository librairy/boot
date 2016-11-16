/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.edges;

import org.librairy.boot.storage.system.graph.domain.edges.DescribesEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DescribesEdgeRepository extends RelationGraphRepository<DescribesEdge> {

    // To avoid a class type exception
    @Override
    DescribesEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:DESCRIBES]->(node2{uri:{1}}) return r")
    Iterable<DescribesEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[:CONTAINS]->(:Part)-[r:DESCRIBES]->(:Item) return r")
    Iterable<DescribesEdge> findByDomain(String uri);

    @Query("match (part{uri:{0}})-[r:DESCRIBES]->(item) return r")
    Iterable<DescribesEdge> findByPart(String uri);

    @Query("match (item{uri:{0}})<-[r:DESCRIBES]->(part) return r")
    Iterable<DescribesEdge> findByItem(String uri);

}
