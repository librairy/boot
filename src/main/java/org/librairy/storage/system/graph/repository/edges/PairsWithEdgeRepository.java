package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.PairsWithEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PairsWithEdgeRepository extends RelationGraphRepository<PairsWithEdge> {

    // To avoid a class type exception
    @Override
    PairsWithEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:PAIRS_WITH]->(node2) return r")
    Iterable<PairsWithEdge> fromNode(String start, String end);

    @Query("match (node1{uri:{0}})-[r:PAIRS_WITH]->(node2{uri:{1}}) return r")
    Iterable<PairsWithEdge> findByNodes(String start, String end);

    @Query("match (:Word)<-[r:PAIRS_WITH{domain:{0}}]-(:Word) return r")
    Iterable<PairsWithEdge> findByDomain(String uri);

    @Query("match (word{uri:{0}})-[r:PAIRS_WITH]-(w) return r")
    Iterable<PairsWithEdge> findByWord(String uri);

}
