/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.domain.resources.*;
import org.librairy.boot.storage.system.column.repository.UnifiedColumnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ColumnConfig.class)
@TestPropertySource(properties = {
        "librairy.columndb.host = 192.168.99.100"
})
public class UnifiedColumnRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedColumnRepositoryTest.class);

    @Autowired
    UnifiedColumnRepository unifiedColumnRepository;


    @Test
    public void domain(){

        Domain domain = Resource.newDomain
                ("d1");
        domain.setUri("domains/01");
        domain.setName("test01");
        domain.setDescription("testing purposes");
        test(domain, Resource.Type.DOMAIN);
    }

    @Test
    public void item() {

        Item resource = Resource.newItem("i1");
        resource.setUri("items/01");
        resource.setAuthoredBy("me");
        resource.setDescription("testing purposes");
        test(resource, Resource.Type.ITEM);
    }

    @Test
    public void part() {

        Part resource = Resource.newPart("p1");
        resource.setUri("items/01");
        resource.setSense("nosense");
        resource.setContent("sampling");
        resource.setTokens("sample");
        test(resource, Resource.Type.PART);
    }

    @Test
    public void topic() {

        Topic resource = Resource.newTopic
                ("t1");
        resource.setUri("items/01");
        resource.setAnalysis("analyses/01");
        resource.setContent("sampling");
        test(resource, Resource.Type.TOPIC);
    }


    private void test(Resource resource, Resource.Type type){

        LOG.info("####################### " + type.name());

        unifiedColumnRepository.deleteAll(type);

        Assert.assertFalse(unifiedColumnRepository.exists(type,resource.getUri()));

        unifiedColumnRepository.save(resource);

        Assert.assertTrue(unifiedColumnRepository.exists(type,resource.getUri()));
        Optional<Resource> result = unifiedColumnRepository.read(type,resource.getUri());
        Assert.assertTrue(result.isPresent());

        Resource resource2 = result.get();

        Assert.assertEquals(resource,result.get());

        unifiedColumnRepository.delete(type,resource.getUri());

        Assert.assertFalse(unifiedColumnRepository.exists(type,resource.getUri()));
    }

}
