package org.librairy.storage.system.graph.edge;

import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Term;
import org.librairy.storage.system.graph.domain.edges.AppearedInEdge;
import org.librairy.storage.system.graph.repository.edges.AppearedInEdgeRepository;
import org.librairy.storage.system.graph.repository.edges.RelationGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
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
        this.domain = org.librairy.model.domain.resources.Resource.newDomain();
        this.term  = org.librairy.model.domain.resources.Resource.newTerm();

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
