package org.librairy.storage.system.graph.repository.nodes;

import org.librairy.storage.exception.RepositoryNotFound;
import org.librairy.storage.system.graph.domain.nodes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedNodeGraphRepositoryFactory {

    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    ItemGraphRepository itemGraphRepository;

    @Autowired
    PartGraphRepository partGraphRepository;

    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    TopicGraphRepository topicGraphRepository;

    @Autowired
    WordGraphRepository wordGraphRepository;

    @Autowired
    TermGraphRepository termGraphRepository;

    @Autowired
    FilterGraphRepository filterGraphRepository;

    @Autowired
    PathGraphRepository pathGraphRepository;


    public ResourceGraphRepository repositoryOf(org.librairy.model.domain.resources.Resource.Type type) throws RepositoryNotFound {
        switch (type){
            case DOCUMENT: return documentGraphRepository;
            case DOMAIN: return domainGraphRepository;
            case ITEM: return itemGraphRepository;
            case PART: return partGraphRepository;
            case SOURCE: return sourceGraphRepository;
            case TOPIC: return topicGraphRepository;
            case WORD: return wordGraphRepository;
            case TERM: return termGraphRepository;
            case FILTER: return filterGraphRepository;
            case PATH: return pathGraphRepository;
        }
        throw new RepositoryNotFound("Graph Repository not found for " + type);
    }

    public Class mappingOf(org.librairy.model.domain.resources.Resource.Type type){
        switch (type){
            case DOCUMENT: return DocumentNode.class;
            case DOMAIN: return DomainNode.class;
            case ITEM: return ItemNode.class;
            case PART: return PartNode.class;
            case SOURCE: return SourceNode.class;
            case TOPIC: return TopicNode.class;
            case WORD: return WordNode.class;
            case TERM: return TermNode.class;
            case FILTER: return FilterNode.class;
            case PATH: return PathNode.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

}
