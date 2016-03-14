package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.DealsWithFromPartEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DealsWithFromPartEdgeRepository extends RelationGraphRepository<DealsWithFromPartEdge> {


    // To avoid a class type exception
    @Override
    DealsWithFromPartEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2) return r")
    Iterable<DealsWithFromPartEdge> fromNode(String start, String end);

    @Query("match (node1{uri:{0}})-[r:DEALS_WITH]->(node2{uri:{1}}) return r")
    Iterable<DealsWithFromPartEdge> findByNodes(String start, String end);

    @Query("match (:Part)-[r:DEALS_WITH]->(:Topic)-[:EMERGES_IN]->(domain{uri:{0}}) return r")
    Iterable<DealsWithFromPartEdge> findByDomain(String uri);

    @Query("match (part{uri:{0}})-[r:DEALS_WITH]->(topic) return r")
    Iterable<DealsWithFromPartEdge> findByPart(String uri);

    @Query("match (part)-[r:DEALS_WITH]->(topic{uri:{0}}) return r")
    Iterable<DealsWithFromPartEdge> findByTopic(String uri);

}
