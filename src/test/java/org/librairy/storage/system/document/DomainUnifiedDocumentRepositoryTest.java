/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.document;

import org.librairy.storage.system.document.domain.DomainDocument;
import org.librairy.storage.system.document.repository.BaseDocumentRepository;
import org.librairy.storage.system.document.repository.DomainDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DomainUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<DomainDocument> {

    @Autowired
    DomainDocumentRepository repository;

    @Override
    public BaseDocumentRepository<DomainDocument> getRepository() {
        return repository;
    }

    @Override
    public DomainDocument getEntity() {
        DomainDocument document = new DomainDocument();
        document.setUri("domains/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setDescription("for testing purposes");
        document.setName("test");
        return document;
    }
}
