/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.nodes;

import org.librairy.boot.storage.system.graph.domain.nodes.ItemNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ItemGraphRepository extends ResourceGraphRepository<ItemNode> {

    // To avoid a class type exception
    @Override
    ItemNode findOneByUri(String uri);

    @Query("match (item)<-[:CONTAINS]-(domain{uri:{0}}) return item")
    Iterable<ItemNode> findByDomain(String uri);

    @Query("match (item)<-[:BUNDLES]-(document{uri:{0}}) return item")
    Iterable<ItemNode> findByDocument(String uri);

    @Query("match (part{uri:{0}})-[:DESCRIBES]->(item) return item")
    Iterable<ItemNode> findByPart(String uri);

    @Query("match (item)-[:DEALS_WITH]->(topic{uri:{0}}) return item")
    Iterable<ItemNode> findByTopic(String uri);

    @Query("match (item)-[:SIMILAR_TO]-(i{uri:{0}}) return item")
    Iterable<ItemNode> findByItem(String uri);

}
