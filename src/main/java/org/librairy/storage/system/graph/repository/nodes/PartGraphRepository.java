/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.nodes;

import org.librairy.storage.system.graph.domain.nodes.PartNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PartGraphRepository extends ResourceGraphRepository<PartNode> {

    // To avoid a class type exception
    @Override
    PartNode findOneByUri(String uri);

    @Query("match (part)-[:DESCRIBES]->(item)<-[:BUNDLES]-(document)<-[:CONTAINS]-(domain{uri:{0}}) return part")
    Iterable<PartNode> findByDomain(String uri);

    @Query("match (part)-[:DESCRIBES]->(item{uri:{0}}) return part")
    Iterable<PartNode> findByItem(String uri);

    @Query("match (part)-[:DEALS_WITH]->(topic{uri:{0}}) return part")
    Iterable<PartNode> findByTopic(String uri);

    @Query("match (part)-[:SIMILAR_TO]-(p{uri:{0}}) return part")
    Iterable<PartNode> findByPart(String uri);

    @Query("match (part)-[:DESCRIBES]->(item)<-[:BUNDLES]-(document{uri:{0}}) return part")
    Iterable<PartNode> findByDocument(String uri);

}
