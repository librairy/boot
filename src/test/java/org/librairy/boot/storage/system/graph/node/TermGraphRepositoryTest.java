/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.node;

import org.librairy.boot.storage.system.graph.domain.nodes.TermNode;
import org.librairy.boot.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.TermGraphRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TermGraphRepositoryTest extends BaseGraphRepositoryTest<TermNode> {

    @Autowired
    TermGraphRepository repository;

    @Override
    public ResourceGraphRepository<TermNode> getRepository() {
        return repository;
    }

    @Override
    public TermNode getEntity() {
        TermNode node = new TermNode();
        node.setUri("terms/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }

    @Test
    public void findByDomain(){

        String domain = "http://librairy.org/domains/d4a5f93d-fc90-453e-a2d5-7ca27dfb4e29";

        Iterable<TermNode> res = repository.findByDomain(domain);
        System.out.println(res);
    }
}
