/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column;

import org.librairy.boot.storage.system.column.domain.SourceColumn;
import org.librairy.boot.storage.system.column.repository.BaseColumnRepository;
import org.librairy.boot.storage.system.column.repository.SourceColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SourceUnifiedColumnRepositoryTest extends BaseColumnRepositoryTest<SourceColumn> {

    @Autowired
    SourceColumnRepository repository;

    @Override
    public BaseColumnRepository<SourceColumn> getRepository() {
        return repository;
    }

    @Override
    public SourceColumn getEntity() {
        SourceColumn column = new SourceColumn();
        column.setUri("sources/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setName("test");
        column.setDescription("for testing purposes");
        column.setUrl("http://librairy.org");
        column.setProtocol("oaipmh");
        return column;
    }
}
