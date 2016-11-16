/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.system.column.domain.*;
import org.librairy.boot.storage.exception.RepositoryNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedColumnRepositoryFactory {

    @Autowired
    AnalysisColumnRepository analysisColumnRepository;

    @Autowired
    DocumentColumnRepository documentColumnRepository;

    @Autowired
    DomainColumnRepository domainColumnRepository;

    @Autowired
    ItemColumnRepository itemColumnRepository;

    @Autowired
    PartColumnRepository partColumnRepository;

    @Autowired
    SourceColumnRepository sourceColumnRepository;

    @Autowired
    TopicColumnRepository topicColumnRepository;

    @Autowired
    WordColumnRepository wordColumnRepository;

    @Autowired
    TermColumnRepository termColumnRepository;

    @Autowired
    FilterColumnRepository filterColumnRepository;

    @Autowired
    PathColumnRepository pathColumnRepository;

    @Autowired
    BundleColumnRepository bundleColumnRepository;

    @Autowired
    AggregateColumnRepository aggregateColumnRepository;

    @Autowired
    AppearedInColumnRepository appearedInColumnRepository;

    @Autowired
    ComposeColumnRepository composeColumnRepository;

    @Autowired
    ContainColumnRepository containColumnRepository;

    @Autowired
    DealsWithColumnRepository dealsWithColumnRepository;

    @Autowired
    DescribesColumnRepository describesColumnRepository;

    @Autowired
    EmbeddedInColumnRepository embeddedInColumnRepository;

    @Autowired
    EmergesInColumnRepository emergesInColumnRepository;

    @Autowired
    HypernymColumnRepository hypernymColumnRepository;

    @Autowired
    MentionsColumnRepository mentionsColumnRepository;

    @Autowired
    PairsWithColumnRepository pairsWithColumnRepository;

    @Autowired
    ProvideColumnRepository provideColumnRepository;

    @Autowired
    SimilarToColumnRepository similarToColumnRepository;

    @Autowired
    SerializedObjectColumnRepository serializedObjectColumnRepository;

    public BaseColumnRepository repositoryOf(Resource.Type type) throws RepositoryNotFound {
        switch (type){
            case ANALYSIS: return analysisColumnRepository;
            case DOCUMENT: return documentColumnRepository;
            case DOMAIN: return domainColumnRepository;
            case ITEM: return itemColumnRepository;
            case PART: return partColumnRepository;
            case SOURCE: return sourceColumnRepository;
            case TOPIC: return topicColumnRepository;
            case WORD: return wordColumnRepository;
            case TERM: return termColumnRepository;
            case FILTER: return filterColumnRepository;
            case PATH: return pathColumnRepository;
            case SERIALIZED_OBJECT: return serializedObjectColumnRepository;
        }
        throw new RepositoryNotFound("Column Repository not found for " + type);
    }

    public BaseColumnRepository repositoryOf(Relation.Type type) throws RepositoryNotFound {
        switch (type){
            case BUNDLES: return bundleColumnRepository;
            case AGGREGATES: return aggregateColumnRepository;
            case APPEARED_IN: return appearedInColumnRepository;
            case COMPOSES: return composeColumnRepository;
            case CONTAINS_TO_DOCUMENT:
            case CONTAINS_TO_ITEM:
            case CONTAINS_TO_PART: return containColumnRepository;
            case DEALS_WITH_FROM_DOCUMENT:
            case DEALS_WITH_FROM_ITEM:
            case DEALS_WITH_FROM_PART: return dealsWithColumnRepository;
            case DESCRIBES: return describesColumnRepository;
            case EMBEDDED_IN: return embeddedInColumnRepository;
            case EMERGES_IN: return emergesInColumnRepository;
            case HYPERNYM_OF: return hypernymColumnRepository;
            case MENTIONS_FROM_TERM:
            case MENTIONS_FROM_TOPIC: return mentionsColumnRepository;
            case PAIRS_WITH: return pairsWithColumnRepository;
            case PROVIDES: return provideColumnRepository;
            case SIMILAR_TO_DOCUMENTS:
            case SIMILAR_TO_ITEMS:
            case SIMILAR_TO_PARTS: return similarToColumnRepository;
        }
        throw new RepositoryNotFound("Column Repository not found for " + type);
    }

    public Class mappingOf(Resource.Type type){
        switch (type){
            case ANALYSIS: return AnalysisColumn.class;
            case DOCUMENT: return DocumentColumn.class;
            case DOMAIN: return DomainColumn.class;
            case ITEM: return ItemColumn.class;
            case PART: return PartColumn.class;
            case SOURCE: return SourceColumn.class;
            case TOPIC: return TopicColumn.class;
            case WORD: return WordColumn.class;
            case TERM: return TermColumn.class;
            case FILTER: return FilterColumn.class;
            case PATH: return PathColumn.class;
            case SERIALIZED_OBJECT: return SerializedObjectColumn.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

    public Class mappingOf(Relation.Type type){
        switch (type){
            case BUNDLES: return BundleColumn.class;
            case AGGREGATES: return AggregateColumn.class;
            case APPEARED_IN: return AppearedInColumn.class;
            case COMPOSES: return ComposeColumn.class;
            case CONTAINS_TO_DOCUMENT:
            case CONTAINS_TO_ITEM:
            case CONTAINS_TO_PART:return ContainColumn.class;
            case DEALS_WITH_FROM_DOCUMENT:
            case DEALS_WITH_FROM_ITEM:
            case DEALS_WITH_FROM_PART: return DealsWithColumn.class;
            case DESCRIBES: return DescribeColumn.class;
            case EMBEDDED_IN: return EmbeddedInColumn.class;
            case EMERGES_IN: return EmergeInColumn.class;
            case HYPERNYM_OF: return HypernymOfColumn.class;
            case MENTIONS_FROM_TERM:
            case MENTIONS_FROM_TOPIC: return MentionsColumn.class;
            case PAIRS_WITH: return PairsWithColumn.class;
            case PROVIDES: return ProvideColumn.class;
            case SIMILAR_TO_DOCUMENTS:
            case SIMILAR_TO_ITEMS:
            case SIMILAR_TO_PARTS: return SimilarToColumn.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

}
