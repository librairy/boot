package org.librairy.storage.system.graph.repository.nodes;

import org.librairy.storage.system.graph.domain.nodes.DomainNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DomainGraphRepository extends ResourceGraphRepository<DomainNode> {

    // To avoid a class type exception
    @Override
    DomainNode findOneByUri(String uri);

    @Query("match (domain{uri:{0}}) return domain")
    Iterable<DomainNode> findByDomain(String uri);

    @Query("match (source{uri:{0}})-[:COMPOSES]->(d:Domain) return d")
    Iterable<DomainNode> findBySource(String uri);

    @Query("match (d:Domain)-[:CONTAINS]->(doc{uri:{0}}) return d")
    Iterable<DomainNode> findByDocument(String uri);

    @Query("match (d:Domain)-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(it{uri:{0}}) return d")
    Iterable<DomainNode> findByItem(String uri);

    @Query("match (d:Domain)-[:CONTAINS]->(doc:Document)-[:BUNDLES]->(it:Item)<-[:DESCRIBES]-(p{uri:{0}}) return d")
    Iterable<DomainNode> findByPart(String uri);

    @Query("match (d:Domain)<-[:EMBEDDED_IN]-(w{uri:{0}}) return d")
    Iterable<DomainNode> findByWord(String uri);

    @Query("match (d:Domain)<-[:APPEARED_IN]-(t{uri:{0}}) return d")
    Iterable<DomainNode> findByTerm(String uri);

    @Query("match (d:Domain)<-[:EMERGES_IN]-(t{uri:{0}}) return d")
    Iterable<DomainNode> findByTopic(String uri);
}
