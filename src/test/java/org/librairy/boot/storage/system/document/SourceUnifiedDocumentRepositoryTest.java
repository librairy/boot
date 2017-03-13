/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document;

import org.librairy.boot.storage.system.document.domain.SourceDocument;
import org.librairy.boot.storage.system.document.repository.BaseDocumentRepository;
import org.librairy.boot.storage.system.document.repository.SourceDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SourceUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<SourceDocument> {

    @Autowired
    SourceDocumentRepository repository;

    @Override
    public BaseDocumentRepository<SourceDocument> getRepository() {
        return repository;
    }

    @Override
    public SourceDocument getEntity() {
        SourceDocument document = new SourceDocument();
        document.setUri("sources/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setName("test");
        document.setDescription("for testing purposes");
        document.setUrl("http://librairy.org");
        document.setProtocol("oaipmh");
        return document;
    }
}