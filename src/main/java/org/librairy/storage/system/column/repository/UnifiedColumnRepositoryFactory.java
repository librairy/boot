package org.librairy.storage.system.column.repository;

import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.exception.RepositoryNotFound;
import org.librairy.storage.system.column.domain.*;
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
    BundleColumnRepository bundleColumnRepository;

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
            case SERIALIZED_OBJECT: return serializedObjectColumnRepository;
        }
        throw new RepositoryNotFound("Column Repository not found for " + type);
    }

    public BaseColumnRepository repositoryOf(Relation.Type type) throws RepositoryNotFound {
        switch (type){
            case BUNDLES: return bundleColumnRepository;
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
            case SERIALIZED_OBJECT: return SerializedObjectColumn.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

    public Class mappingOf(Relation.Type type){
        switch (type){
            case BUNDLES: return BundleColumn.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }

}
