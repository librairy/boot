/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.nodes;

import org.librairy.boot.storage.system.graph.domain.nodes.WordNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordGraphRepository extends ResourceGraphRepository<WordNode> {

    @Override
    WordNode findOneByUri(String uri);

    @Query("match (w)-[:PAIRS_WITH]->(word{uri:{0}}) return w")
    Iterable<WordNode> findByWord(String uri);

    @Query("match (w:Word)<-[:MENTIONS]-(e)-[:EMERGES_IN|APPEARED_IN]->(domain{uri:{0}}) return w UNION match (w:Word)-[:EMBEDDED_IN]->(domain{uri:{0}}) return w")

    Iterable<WordNode> findByDomain(String uri);

    @Query("match (w:Word)<-[:MENTIONS]-(term{uri:{0}}) return w")
    Iterable<WordNode> findByTerm(String uri);

    @Query("match (w:Word)<-[:MENTIONS]-(topic{uri:{0}}) return w")
    Iterable<WordNode> findByTopic(String uri);

    @Query("match (in:Word)-[s{domain:{0}}]->(out:Word) delete s")
    void deletePairingInDomain(String uri);

    @Query("match (in:Word)-[e:EMBEDDED_IN]->(domain{uri:{0}}) delete e")
    void deleteEmbeddingInDomain(String uri);




}
