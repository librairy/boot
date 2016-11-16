/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column;

import org.librairy.boot.model.domain.LinkableElement;
import org.librairy.boot.storage.system.column.domain.SerializedObjectColumn;
import org.librairy.boot.storage.system.column.repository.BaseColumnRepository;
import org.librairy.boot.storage.system.column.repository.SerializedObjectColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SerializedObjectUnifiedColumnRepositoryTest extends BaseColumnRepositoryTest<SerializedObjectColumn> {

    @Autowired
    SerializedObjectColumnRepository repository;

    @Override
    public BaseColumnRepository<SerializedObjectColumn> getRepository() {
        return repository;
    }

    @Override
    public SerializedObjectColumn getEntity() {

        LinkableElement sample = new LinkableElement();
        sample.setUri("sample");
        sample.setCreationTime("2016");



        SerializedObjectColumn column = new SerializedObjectColumn();
        column.setUri("serializations/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setInstance(sample);
        return column;
    }

}
