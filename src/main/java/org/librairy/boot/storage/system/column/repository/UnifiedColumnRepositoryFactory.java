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
    DomainColumnRepository domainColumnRepository;

    @Autowired
    ItemColumnRepository itemColumnRepository;

    @Autowired
    PartColumnRepository partColumnRepository;

    @Autowired
    TopicColumnRepository topicColumnRepository;

    @Autowired
    FilterColumnRepository filterColumnRepository;

    @Autowired
    PathColumnRepository pathColumnRepository;

    @Autowired
    ContainColumnRepository containColumnRepository;

    @Autowired
    DealsWithColumnRepository dealsWithColumnRepository;

    @Autowired
    DescribesColumnRepository describesColumnRepository;

    @Autowired
    EmergesInColumnRepository emergesInColumnRepository;

    @Autowired
    SimilarToColumnRepository similarToColumnRepository;

    @Autowired
    AnnotationColumnRepository annotationColumnRepository;

    @Autowired
    ListenerColumnRepository listenerColumnRepository;

    public BaseColumnRepository repositoryOf(Resource.Type type) throws RepositoryNotFound {
        switch (type){
            case DOMAIN: return domainColumnRepository;
            case ITEM: return itemColumnRepository;
            case PART: return partColumnRepository;
            case TOPIC: return topicColumnRepository;
            case FILTER: return filterColumnRepository;
            case PATH: return pathColumnRepository;
            case ANNOTATION: return annotationColumnRepository;
            case LISTENER: return listenerColumnRepository;
        }
        throw new RepositoryNotFound("Column Repository not found for " + type);
    }

    public BaseColumnRepository repositoryOf(Relation.Type type) throws RepositoryNotFound {
        switch (type){
            case CONTAINS_TO_ITEM:
            case CONTAINS_TO_PART: return containColumnRepository;
            case DEALS_WITH_FROM_ITEM:
            case DEALS_WITH_FROM_PART: return dealsWithColumnRepository;
            case DESCRIBES: return describesColumnRepository;
            case EMERGES_IN: return emergesInColumnRepository;
            case SIMILAR_TO_ITEMS:
            case SIMILAR_TO_PARTS: return similarToColumnRepository;
        }
        throw new RepositoryNotFound("Column Repository not found for " + type);
    }

    public Class mappingOf(Resource.Type type){
        switch (type){
            case DOMAIN: return DomainColumn.class;
            case ITEM: return ItemColumn.class;
            case PART: return PartColumn.class;
            case TOPIC: return TopicColumn.class;
            case FILTER: return FilterColumn.class;
            case PATH: return PathColumn.class;
            case ANNOTATION: return AnnotationColumn.class;
            case LISTENER: return ListenerColumn.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

    public Class mappingOf(Relation.Type type){
        switch (type){
            case CONTAINS_TO_ITEM:
            case CONTAINS_TO_PART:return ContainColumn.class;
            case DEALS_WITH_FROM_ITEM:
            case DEALS_WITH_FROM_PART: return DealsWithColumn.class;
            case DESCRIBES: return DescribeColumn.class;
            case EMERGES_IN: return EmergeInColumn.class;
            case SIMILAR_TO_ITEMS:
            case SIMILAR_TO_PARTS: return SimilarToColumn.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

}
