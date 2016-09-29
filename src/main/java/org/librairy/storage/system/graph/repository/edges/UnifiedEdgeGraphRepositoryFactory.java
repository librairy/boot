/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.edges;

import org.librairy.storage.exception.RepositoryNotFound;
import org.librairy.storage.system.graph.domain.edges.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedEdgeGraphRepositoryFactory {

    @Autowired
    DealsWithFromDocumentEdgeRepository dealsWithFromDocumentEdgeRepository;

    @Autowired
    BundlesEdgeRepository bundlesEdgeRepository;

    @Autowired
    SimilarToEdgeRepository similarToEdgeRepository;

    @Autowired
    ContainsEdgeRepository containsEdgeRepository;

    @Autowired
    DealsWithFromItemEdgeRepository dealsWithFromItemEdgeRepository;

    @Autowired
    DealsWithFromPartEdgeRepository dealsWithFromPartEdgeRepository;

    @Autowired
    DescribesEdgeRepository describesEdgeRepository;

    @Autowired
    ComposesEdgeRepository composesEdgeRepository;

    @Autowired
    ProvidesEdgeRepository providesEdgeRepository;

    @Autowired
    EmergesInEdgeRepository emergesInEdgeRepository;

    @Autowired
    TopicMentionsWordGraphRepository topicMentionsWordGraphRepository;

    @Autowired
    EmbeddedInEdgeRepository embeddedInEdgeRepository;

    @Autowired
    PairsWithEdgeRepository pairsWithEdgeRepository;

    @Autowired
    AppearedInEdgeRepository appearedInEdgeRepository;

    @Autowired
    MentionsFromTermEdgeRepository mentionsFromTermEdgeRepository;

    @Autowired
    HypernymOfEdgeRepository hypernymOfEdgeRepository;


    public RelationGraphRepository repositoryOf(org.librairy.model.domain.relations.Relation.Type type) throws RepositoryNotFound{
        switch (type){
            case BUNDLES: return bundlesEdgeRepository;
            case DEALS_WITH_FROM_DOCUMENT: return dealsWithFromDocumentEdgeRepository;
            case SIMILAR_TO_DOCUMENTS: return similarToEdgeRepository;
            case CONTAINS: return containsEdgeRepository;
            case DEALS_WITH_FROM_ITEM: return dealsWithFromItemEdgeRepository;
            case SIMILAR_TO_ITEMS: return similarToEdgeRepository;
            case DEALS_WITH_FROM_PART: return dealsWithFromPartEdgeRepository;
            case DESCRIBES: return describesEdgeRepository;
            case SIMILAR_TO_PARTS: return similarToEdgeRepository;
            case COMPOSES: return composesEdgeRepository;
            case PROVIDES: return providesEdgeRepository;
            case EMERGES_IN: return emergesInEdgeRepository;
            case MENTIONS_FROM_TOPIC: return topicMentionsWordGraphRepository;
            case EMBEDDED_IN: return embeddedInEdgeRepository;
            case PAIRS_WITH: return pairsWithEdgeRepository;
            case APPEARED_IN: return appearedInEdgeRepository;
            case MENTIONS_FROM_TERM: return mentionsFromTermEdgeRepository;
            case HYPERNYM_OF: return hypernymOfEdgeRepository;
        }
        throw new RepositoryNotFound("Graph Repository not found for " + type);
    }

    public Class mappingOf(org.librairy.model.domain.relations.Relation.Type type){
        switch (type){
            case BUNDLES: return BundlesEdge.class;
            case DEALS_WITH_FROM_DOCUMENT: return DealsWithFromDocumentEdge.class;
            case SIMILAR_TO_DOCUMENTS: return SimilarToDocumentsEdge.class;
            case CONTAINS: return ContainsEdge.class;
            case DEALS_WITH_FROM_ITEM: return DealsWithFromItemEdge.class;
            case SIMILAR_TO_ITEMS: return SimilarToItemsEdge.class;
            case DEALS_WITH_FROM_PART: return DealsWithFromPartEdge.class;
            case DESCRIBES: return DescribesEdge.class;
            case SIMILAR_TO_PARTS: return SimilarToPartsEdge.class;
            case COMPOSES: return ComposesEdge.class;
            case PROVIDES: return ProvidesEdge.class;
            case EMERGES_IN: return EmergesInEdge.class;
            case MENTIONS_FROM_TOPIC: return MentionsFromTopicEdge.class;
            case EMBEDDED_IN: return EmbeddedInEdge.class;
            case PAIRS_WITH: return PairsWithEdge.class;
            case APPEARED_IN: return AppearedInEdge.class;
            case MENTIONS_FROM_TERM: return MentionsFromTermEdge.class;
            case HYPERNYM_OF: return HypernymOfEdge.class;
            default: throw new RuntimeException("No relation found for type: " + type);
        }

    }

}
