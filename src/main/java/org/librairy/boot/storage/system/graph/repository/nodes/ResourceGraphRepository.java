/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.nodes;

import org.librairy.boot.storage.system.graph.domain.nodes.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface ResourceGraphRepository<T extends Node> extends GraphRepository<T>{

    T findOneByUri(String uri);

    @Query("match (n) return n")
    Iterable<T> findByDomain(String uri);
}
