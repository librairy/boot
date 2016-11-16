/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.node;

import org.librairy.boot.storage.system.graph.domain.nodes.DomainNode;
import org.librairy.boot.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DomainGraphRepositoryTest extends BaseGraphRepositoryTest<DomainNode> {

    @Autowired
    DomainGraphRepository repository;

    @Override
    public ResourceGraphRepository<DomainNode> getRepository() {
        return repository;
    }

    @Override
    public DomainNode getEntity() {
        DomainNode node = new DomainNode();
        node.setUri("domains/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }
}
