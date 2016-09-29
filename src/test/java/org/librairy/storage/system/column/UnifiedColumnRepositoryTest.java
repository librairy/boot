/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column;

import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.storage.system.column.repository.UnifiedColumnRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
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
    public void source(){

        org.librairy.model.domain.resources.Source source = org.librairy.model.domain.resources.Resource.newSource
                ("s1");
        source.setUri("sources/01");
        source.setName("test01");
        source.setDescription("testing purposes");
        test(source, org.librairy.model.domain.resources.Resource.Type.SOURCE);
    }

    @Test
    public void domain(){

        org.librairy.model.domain.resources.Domain domain = org.librairy.model.domain.resources.Resource.newDomain
                ("d1");
        domain.setUri("domains/01");
        domain.setName("test01");
        domain.setDescription("testing purposes");
        test(domain, org.librairy.model.domain.resources.Resource.Type.DOMAIN);
    }

    @Test
    public void document() {

        org.librairy.model.domain.resources.Document document = org.librairy.model.domain.resources.Resource
                .newDocument("d1");
        document.setUri("documents/01");
        document.setAuthoredBy("me");
        document.setAuthoredOn("20151210");
        document.setDescription("testing purposes");
        test(document, org.librairy.model.domain.resources.Resource.Type.DOCUMENT);
    }

    @Test
    public void item() {

        org.librairy.model.domain.resources.Item resource = org.librairy.model.domain.resources.Resource.newItem("i1");
        resource.setUri("items/01");
        resource.setAuthoredBy("me");
        resource.setDescription("testing purposes");
        test(resource, org.librairy.model.domain.resources.Resource.Type.ITEM);
    }

    @Test
    public void part() {

        org.librairy.model.domain.resources.Part resource = org.librairy.model.domain.resources.Resource.newPart("p1");
        resource.setUri("items/01");
        resource.setSense("nosense");
        resource.setContent("sampling");
        resource.setTokens("sample");
        test(resource, org.librairy.model.domain.resources.Resource.Type.PART);
    }

    @Test
    public void topic() {

        org.librairy.model.domain.resources.Topic resource = org.librairy.model.domain.resources.Resource.newTopic
                ("t1");
        resource.setUri("items/01");
        resource.setAnalysis("analyses/01");
        resource.setContent("sampling");
        test(resource, org.librairy.model.domain.resources.Resource.Type.TOPIC);
    }

    @Test
    public void word() {

        org.librairy.model.domain.resources.Word resource = org.librairy.model.domain.resources.Resource.newWord("w1");
        resource.setUri("items/01");
        resource.setContent("house");
        test(resource, org.librairy.model.domain.resources.Resource.Type.WORD);
    }


    private void test(org.librairy.model.domain.resources.Resource resource, org.librairy.model.domain.resources.Resource.Type type){

        LOG.info("####################### " + type.name());

        unifiedColumnRepository.deleteAll(type);

        Assert.assertFalse(unifiedColumnRepository.exists(type,resource.getUri()));

        unifiedColumnRepository.save(resource);

        Assert.assertTrue(unifiedColumnRepository.exists(type,resource.getUri()));
        Optional<org.librairy.model.domain.resources.Resource> result = unifiedColumnRepository.read(type,resource.getUri());
        Assert.assertTrue(result.isPresent());

        org.librairy.model.domain.resources.Resource resource2 = result.get();

        Assert.assertEquals(resource,result.get());

        unifiedColumnRepository.delete(type,resource.getUri());

        Assert.assertFalse(unifiedColumnRepository.exists(type,resource.getUri()));
    }


    @Test
    public void findBy(){

        unifiedColumnRepository.deleteAll(org.librairy.model.domain.resources.Resource.Type.DOCUMENT);

        org.librairy.model.domain.resources.Document document = new org.librairy.model.domain.resources.Document();
        document.setUri("documents/01");
        document.setTitle("This is a title");
        unifiedColumnRepository.save(document);

        Iterable<org.librairy.model.domain.resources.Resource> documents = unifiedColumnRepository.findBy(org.librairy.model.domain.resources.Resource.Type.DOCUMENT, "title", document.getTitle());
        System.out.println("Documents: " + documents);

    }
}
