package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.ContainsEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ContainsEdgeRepository extends RelationGraphRepository<ContainsEdge> {

    // To avoid a class type exception
    @Override
    ContainsEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:CONTAINS]->(node2{uri:{1}}) return r")
    Iterable<ContainsEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})-[r:CONTAINS]->(:Document) return r")
    Iterable<ContainsEdge> findByDomain(String uri);

    @Query("match (domain)-[r:CONTAINS]->(document{uri:{0}}) return r")
    Iterable<ContainsEdge> findByDocument(String uri);

}
