package org.librairy.storage.system.graph.repository.nodes;

import org.librairy.storage.system.graph.domain.nodes.TopicNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicGraphRepository extends ResourceGraphRepository<TopicNode> {

    // To avoid a class type exception
    @Override
    TopicNode findOneByUri(String uri);

    @Query("match (topic)-[:EMERGES_IN]->(domain{uri:{0}}) return topic")
    Iterable<TopicNode> findByDomain(String uri);

    @Query("match (topic)-[:MENTIONS]->(word{uri:{0}}) return topic")
    Iterable<TopicNode> findByWord(String uri);

    @Query("match (topic)<-[:DEALS_WITH]-(part{uri:{0}}) return topic")
    Iterable<TopicNode> findByPart(String uri);

    @Query("match (topic)<-[:DEALS_WITH]-(document{uri:{0}}) return topic")
    Iterable<TopicNode> findByDocument(String uri);

    @Query("match (topic)<-[:DEALS_WITH]-(item{uri:{0}}) return topic")
    Iterable<TopicNode> findByItem(String uri);

    @Query("match (topic)-[:MENTIONS]->(word)<-[:MENTIONS]-(term{uri:{0}}) return topic")
    Iterable<TopicNode> findByTerm(String uri);

}
