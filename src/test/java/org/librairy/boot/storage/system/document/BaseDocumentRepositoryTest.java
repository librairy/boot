/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document;

import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.system.document.DocumentConfig;
import org.librairy.boot.storage.system.document.repository.BaseDocumentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 21/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DocumentConfig.class)
@TestPropertySource(properties = { "librairy.eventbus.host = local","librairy.elasticsearch.contactpoints = 192.168" +
        ".99.100", "librairy.elasticsearch.port = 5021" })
public abstract class BaseDocumentRepositoryTest<T extends Resource> {

    public abstract BaseDocumentRepository<T> getRepository();

    public abstract T getEntity();

    @Test
    public void crud(){
        long count1 = getRepository().count();

        T entity = getEntity();
        T column = getRepository().save(entity);
        Assert.assertEquals(entity, column);

        long count2 = getRepository().count();
        Assert.assertEquals(count1 + 1l, count2);

        getRepository().delete(entity);

        long count3 = getRepository().count();
        Assert.assertEquals(count1, count3);
    }


    @Test
    public void findOne(){
        T entity = getEntity();

        T found = getRepository().findOne(entity.getUri());
        Assert.assertNull(found);

        getRepository().save(entity);

        found = getRepository().findOne(entity.getUri());
        Assert.assertNotNull(found);

        getRepository().delete(found);
    }


}
