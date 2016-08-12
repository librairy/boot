package org.librairy.storage.system.graph.node;

import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.storage.system.graph.GraphConfig;
import org.librairy.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
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
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = {
        "librairy.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "librairy.neo4j.port = 5030" })
public class UnifiedGraphRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedGraphRepositoryTest.class);

    @Autowired
    UnifiedNodeGraphRepository repository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Test
    public void source(){

        org.librairy.model.domain.resources.Source source = new org.librairy.model.domain.resources.Source();
        source.setUri("sources/01");
        source.setName("test01");
        source.setDescription("testing purposes");
        test(source, org.librairy.model.domain.resources.Resource.Type.SOURCE);
    }

    @Test
    public void domain(){

        org.librairy.model.domain.resources.Domain domain = new org.librairy.model.domain.resources.Domain();
        domain.setUri("domains/01");
        domain.setName("test01");
        domain.setDescription("testing purposes");
        test(domain, org.librairy.model.domain.resources.Resource.Type.DOMAIN);
    }

    @Test
    public void document() {

        org.librairy.model.domain.resources.Document document = new org.librairy.model.domain.resources.Document();
        document.setUri("documents/01");
        document.setAuthoredBy("me");
        document.setAuthoredOn("20151210");
        document.setDescription("testing purposes");
        test(document, org.librairy.model.domain.resources.Resource.Type.DOCUMENT);
    }

    @Test
    public void item() {

        org.librairy.model.domain.resources.Item resource = new org.librairy.model.domain.resources.Item();
        resource.setUri("items/01");
        resource.setAuthoredBy("me");
        resource.setDescription("testing purposes");
        test(resource, org.librairy.model.domain.resources.Resource.Type.ITEM);
    }

    @Test
    public void part() {

        org.librairy.model.domain.resources.Part resource = new org.librairy.model.domain.resources.Part();
        resource.setUri("parts/01");
        resource.setSense("nosense");
        resource.setContent("sampling");
        resource.setTokens("sample");
        test(resource, org.librairy.model.domain.resources.Resource.Type.PART);
    }

    @Test
    public void topic() {

        org.librairy.model.domain.resources.Topic resource = new org.librairy.model.domain.resources.Topic();
        resource.setUri("topics/01");
        resource.setAnalysis("analyses/01");
        resource.setContent("sampling");
        test(resource, org.librairy.model.domain.resources.Resource.Type.TOPIC);
    }

    @Test
    public void word() {

        org.librairy.model.domain.resources.Word resource = new org.librairy.model.domain.resources.Word();
        resource.setUri("words/01");
        resource.setContent("house");
        test(resource, org.librairy.model.domain.resources.Resource.Type.WORD);
    }


    private void test(org.librairy.model.domain.resources.Resource resource, org.librairy.model.domain.resources.Resource.Type type){

        LOG.info("####################### " + type.name());

        repository.deleteAll(type);

        Assert.assertFalse(repository.exists(type,resource.getUri()));

        repository.save(resource);

        Assert.assertTrue(repository.exists(type,resource.getUri()));
        Optional<org.librairy.model.domain.resources.Resource> result = repository.read(type,resource.getUri());
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(resource,result.get());

        repository.delete(type,resource.getUri());

        Assert.assertFalse(repository.exists(type,resource.getUri()));
    }

}
