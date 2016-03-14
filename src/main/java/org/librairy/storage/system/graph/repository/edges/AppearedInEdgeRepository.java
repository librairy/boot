package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.AppearedInEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface AppearedInEdgeRepository extends RelationGraphRepository<AppearedInEdge> {

    @Override
    AppearedInEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:APPEARED_IN]->(node2{uri:{1}}) return r")
    Iterable<AppearedInEdge> findByNodes(String start, String end);

    @Query("match (domain{uri:{0}})<-[r:APPEARED_IN]-(term) return r")
    Iterable<AppearedInEdge> findByDomain(String uri);

    @Query("match (term{uri:{0}})-[r:APPEARED_IN]->(domain) return r")
    Iterable<AppearedInEdge> findByTerm(String uri);
}
