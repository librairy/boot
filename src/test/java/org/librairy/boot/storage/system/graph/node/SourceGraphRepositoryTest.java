/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.node;

import org.librairy.boot.storage.system.graph.domain.nodes.SourceNode;
import org.librairy.boot.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.SourceGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SourceGraphRepositoryTest extends BaseGraphRepositoryTest<SourceNode> {

    @Autowired
    SourceGraphRepository repository;

    @Override
    public ResourceGraphRepository<SourceNode> getRepository() {
        return repository;
    }

    @Override
    public SourceNode getEntity() {
        SourceNode node = new SourceNode();
        node.setUri("sources/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }

}
