package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.DealsWithFromItemEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DealsWithFromItemEdgeRepository extends RelationGraphRepository<DealsWithFromItemEdge> {

    // To avoid a class type exception
    @Override
    DealsWithFromItemEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2) return r")
    Iterable<DealsWithFromItemEdge> fromNode(String start, String end);

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2{uri:{1}}) return r")
    Iterable<DealsWithFromItemEdge> findByNodes(String start, String end);

    @Query("match (:Item)-[r:DEALS_WITH]->(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<DealsWithFromItemEdge> findByDomain(String uri);

    @Query("match (item{uri:{0}})-[r:DEALS_WITH]->(topic) return r")
    Iterable<DealsWithFromItemEdge> findByItem(String uri);

    @Query("match (item)-[r:DEALS_WITH]->(topic{uri:{0}}) return r")
    Iterable<DealsWithFromItemEdge> findByTopic(String uri);

}
