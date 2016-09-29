/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column;

import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.system.column.domain.TermColumn;
import org.librairy.storage.system.column.repository.BaseColumnRepository;
import org.librairy.storage.system.column.repository.TermColumnRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.support.BasicMapId;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TermUnifiedColumnRepositoryTest extends BaseColumnRepositoryTest<TermColumn> {

    @Autowired
    TermColumnRepository repository;

    @Override
    public BaseColumnRepository<TermColumn> getRepository() {
        return repository;
    }

    @Override
    public TermColumn getEntity() {
        TermColumn term = new TermColumn();
        term.setUri("terms/72ce5395-6268-439a-947e-802229e7f022");
        term.setCreationTime("2015-12-21T16:18:59Z");
        term.setContent("molecular");
        return term;
    }

    @Test
    public void findByContent(){

        String termURI = "terms/72ce5395-6268-439a-947e-802229e7f022";
        repository.delete(BasicMapId.id(ResourceUtils.URI,termURI));

        Iterable<TermColumn> res1 = repository.findByContent("samples");
        Assert.assertFalse(res1.iterator().hasNext());

        TermColumn sample = new TermColumn();
        sample.setUri(termURI);
        sample.setCreationTime("20160112T1533");
        sample.setContent("samples");
        repository.save(sample);

        Iterable<TermColumn> res2 = repository.findByContent("samples");
        Assert.assertTrue(res2.iterator().hasNext());

        // undo
        repository.delete(BasicMapId.id(ResourceUtils.URI,termURI));

    }

}
