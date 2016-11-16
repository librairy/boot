/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document;

import org.librairy.boot.storage.system.document.domain.WordDocument;
import org.librairy.boot.storage.system.document.repository.BaseDocumentRepository;
import org.librairy.boot.storage.system.document.repository.WordDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class WordUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<WordDocument> {

    @Autowired
    WordDocumentRepository repository;

    @Override
    public BaseDocumentRepository<WordDocument> getRepository() {
        return repository;
    }

    @Override
    public WordDocument getEntity() {
        WordDocument document = new WordDocument();
        document.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setContent("molecular");
        return document;
    }
}
