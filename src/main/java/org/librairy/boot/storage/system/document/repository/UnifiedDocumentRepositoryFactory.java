/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document.repository;

import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.exception.RepositoryNotFound;
import org.librairy.boot.storage.system.document.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedDocumentRepositoryFactory {

    @Autowired
    AnalysisDocumentRepository analysisDocumentRepository;

    @Autowired
    DocumentDocumentRepository documentDocumentRepository;

    @Autowired
    DomainDocumentRepository domainDocumentRepository;

    @Autowired
    ItemDocumentRepository itemDocumentRepository;

    @Autowired
    PartDocumentRepository partDocumentRepository;

    @Autowired
    SourceDocumentRepository sourceDocumentRepository;

    @Autowired
    TopicDocumentRepository topicDocumentRepository;

    @Autowired
    WordDocumentRepository wordDocumentRepository;

    @Autowired
    TermDocumentRepository termDocumentRepository;

    @Autowired
    FilterDocumentRepository filterDocumentRepository;

    @Autowired
    PathDocumentRepository pathDocumentRepository;

    public BaseDocumentRepository repositoryOf(Resource.Type type) throws RepositoryNotFound {
        switch (type){
            case ANALYSIS: return analysisDocumentRepository;
            case DOCUMENT: return documentDocumentRepository;
            case DOMAIN: return domainDocumentRepository;
            case ITEM: return itemDocumentRepository;
            case PART: return partDocumentRepository;
            case SOURCE: return sourceDocumentRepository;
            case TOPIC: return topicDocumentRepository;
            case WORD: return wordDocumentRepository;
            case TERM: return termDocumentRepository;
            case FILTER: return filterDocumentRepository;
            case PATH: return pathDocumentRepository;
        }
        throw new RepositoryNotFound("Document Repository not found for " + type);
    }

    public Class mappingOf(Resource.Type type){
        switch (type){
            case ANALYSIS: return AnalysisDocument.class;
            case DOCUMENT: return DocumentDocument.class;
            case DOMAIN: return DomainDocument.class;
            case ITEM: return ItemDocument.class;
            case PART: return PartDocument.class;
            case SOURCE: return SourceDocument.class;
            case TOPIC: return TopicDocument.class;
            case WORD: return WordDocument.class;
            case TERM: return TermDocument.class;
            case FILTER: return FilterDocument.class;
            case PATH: return PathDocument.class;
        }
        throw new RuntimeException("Mapping not found for " + type);
    }


}
