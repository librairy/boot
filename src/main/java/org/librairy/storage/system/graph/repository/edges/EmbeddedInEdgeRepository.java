package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.EmbeddedInEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface EmbeddedInEdgeRepository extends RelationGraphRepository<EmbeddedInEdge> {

    // To avoid a class type exception
    @Override
    EmbeddedInEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:EMBEDDED_IN]->(node2{uri:{1}}) return r")
    Iterable<EmbeddedInEdge> findByNodes(String start, String end);

    @Query("match (:Word)-[r:EMBEDDED_IN]->(domain{uri:{0}}) return r")
    Iterable<EmbeddedInEdge> findByDomain(String uri);

    @Query("match (word{uri:{0}})-[r:EMBEDDED_IN]->(domain) return r")
    Iterable<EmbeddedInEdge> findByWord(String uri);

}
