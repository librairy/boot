/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.node;

import org.librairy.storage.system.graph.domain.nodes.PartNode;
import org.librairy.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.PartGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class PartGraphRepositoryTest extends BaseGraphRepositoryTest<PartNode> {

    @Autowired
    PartGraphRepository repository;

    @Override
    public ResourceGraphRepository<PartNode> getRepository() {
        return repository;
    }

    @Override
    public PartNode getEntity() {
        PartNode node = new PartNode();
        node.setUri("parts/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }
}
