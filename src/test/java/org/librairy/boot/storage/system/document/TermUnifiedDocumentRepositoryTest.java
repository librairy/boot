/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document;

import org.librairy.boot.storage.system.document.domain.TermDocument;
import org.librairy.boot.storage.system.document.repository.BaseDocumentRepository;
import org.librairy.boot.storage.system.document.repository.TermDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TermUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<TermDocument> {

    @Autowired
    TermDocumentRepository repository;

    @Override
    public BaseDocumentRepository<TermDocument> getRepository() {
        return repository;
    }

    @Override
    public TermDocument getEntity() {
        TermDocument document = new TermDocument();
        document.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setContent("molecular");
        return document;
    }
}