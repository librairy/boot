/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.system.graph.domain.edges.Edge;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface RelationGraphRepository<T extends Edge> extends GraphRepository<T>{

    T findOneByUri(String uri);

//    Iterable<T> findByNodes(String startUri, String endUri);
//
//    Iterable<T> findByDomain(String uri);
}
