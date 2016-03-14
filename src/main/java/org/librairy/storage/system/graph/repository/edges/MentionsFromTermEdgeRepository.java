package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.MentionsFromTermEdge;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface MentionsFromTermEdgeRepository extends RelationGraphRepository<MentionsFromTermEdge> {

    // To avoid a class type exception
    @Override
    MentionsFromTermEdge findOneByUri(String uri);

    @Query("match (node1{uri:{0}})-[r:MENTIONS]->(node2{uri:{1}}) return r")
    Iterable<MentionsFromTermEdge> findByNodes(String start, String end);

    @Query("match (:Word)<-[r:MENTIONS]-(:Term)-[:APPEARED_IN]->(domain{uri:{0}}) return r")
    Iterable<MentionsFromTermEdge> findByDomain(String uri);

    @Query("match (term{uri:{0}})-[r:MENTIONS]->(word) return r")
    Iterable<MentionsFromTermEdge> findByTerm(String uri);

    @Query("match (term)-[r:MENTIONS]->(word{uri:{0}}) return r")
    Iterable<MentionsFromTermEdge> findByWord(String uri);

}
