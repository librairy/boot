/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.edge;

import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.domain.resources.Term;
import org.librairy.boot.storage.system.graph.domain.edges.AppearedInEdge;
import org.librairy.boot.storage.system.graph.repository.edges.AppearedInEdgeRepository;
import org.librairy.boot.storage.system.graph.repository.edges.RelationGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class AppearedInGraphRepositoryTest extends BaseGraphRepositoryTest<AppearedInEdge> {

    private static final Logger LOG = LoggerFactory.getLogger(AppearedInGraphRepositoryTest.class);

    @Autowired
    AppearedInEdgeRepository repository;

    @Autowired
    UnifiedNodeGraphRepository nodeRepository;

    Domain domain;

    Term term;

    public void setup(){
        this.domain = Resource.newDomain("d");
        this.term  = Resource.newTerm("t");

        nodeRepository.save(domain);
        nodeRepository.save(term);
    }

    public void shutdown(){
        nodeRepository.delete(domain.getResourceType(),domain.getUri());
        nodeRepository.delete(term.getResourceType(),term.getUri());
    }

    @Override
    public RelationGraphRepository<AppearedInEdge> getRepository() {
        return repository;
    }

    @Override
    public AppearedInEdge getEntity() {
        AppearedInEdge edge = new AppearedInEdge();
        edge.setStartUri(term.getUri());
        edge.setEndUri(domain.getUri());
        return edge;
    }

}
