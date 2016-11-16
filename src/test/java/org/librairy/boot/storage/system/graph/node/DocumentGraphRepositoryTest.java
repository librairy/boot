/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.node;

import org.librairy.boot.storage.system.graph.domain.nodes.DocumentNode;
import org.librairy.boot.storage.system.graph.repository.edges.DealsWithFromDocumentEdgeRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.TopicGraphRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DocumentGraphRepositoryTest extends BaseGraphRepositoryTest<DocumentNode> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentGraphRepositoryTest.class);

    @Autowired
    DocumentGraphRepository repository;

    @Autowired
    DealsWithFromDocumentEdgeRepository dealsWithFromDocumentEdgeRepository;

    @Autowired
    DomainGraphRepository domainRepository;

    @Autowired
    TopicGraphRepository topicGraphRepository;

    @Override
    public ResourceGraphRepository<DocumentNode> getRepository() {
        return repository;
    }

    @Override
    public DocumentNode getEntity() {
        DocumentNode node = new DocumentNode();
        node.setUri("documents/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }

}
