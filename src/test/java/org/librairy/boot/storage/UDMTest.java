/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.Config;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.columndb.host= localhost",
        "librairy.columndb.port= 9042",
        "librairy.eventbus.host = local"
})
public class UDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMTest.class);

    @Autowired
    UDM udm;

    @Autowired
    Helper helper;

    @Autowired
    EventBus eventBus;

    @Autowired
    URIGenerator uriGenerator;

    @Test
    public void readDomains(){
        List<Resource> resources = udm.find(Resource.Type.DOMAIN).all();
        System.out.println(resources);
    }

    @Test
    public void summary(){
        Arrays.stream(Resource.Type.values()).forEach(type -> System.out.println(">"+type.name() + ": " + udm.find(type).all().size() ));
        Arrays.stream(Relation.Type.values()).forEach(type -> System.out.println(">"+type.name() + ": " + udm.find(type).all().size() ));
    }


    @Test
    public void purge(){

        udm.delete(Resource.Type.ANY).all();
        udm.delete(Relation.Type.ANY).all();
    }


    @Test
    public void relations(){

        _saveRelation(Relation.newContains("u1","u2"));
        _saveRelation(Relation.newContains("u1","u2"));
        _saveRelation(Relation.newDealsWithFromItem("i1","i2"));
        _saveRelation(Relation.newDealsWithFromPart("p1","p2"));
        _saveRelation(Relation.newDescribes("u1","u2"));
    }

    private void _saveRelation(Relation relation){
        LOG.info("Saving: " + relation.getType() + " ...");
        relation.setUri(UUID.randomUUID().toString());
        helper.getUnifiedColumnRepository().save(relation);
        LOG.info("Saved: " + relation.getType());
    }


    @Test

    public void findFrom(){
        String uri = "http://drinventor.eu/items/a5179367d5ebf825f01d9247dacae66";
        List<Resource> domains = udm.find(Resource.Type.DOMAIN).from(Resource.Type.ITEM, uri);
        System.out.println("Domains: " + domains);
    }


    @Test
    public void getTopicDistributionOfDocumentsInDomain(){

        String domain = "http://librairy.org/domains/72dba453-eaba-4cb6-99f3-a456e96f3768";

        Iterable<Relation> relations = udm.find(Relation.Type.DEALS_WITH_FROM_ITEM).from(Resource.Type.DOMAIN, domain);
        LOG.info("Result: " + relations);

    }

    public void deleteAll(){
        udm.delete(Resource.Type.ANY).all();
    }



    @Test
    public void readADomain(){

        Optional<Resource>  domain = udm.read(Resource.Type.DOMAIN).byUri("http://librairy.org/domains/4830d78e-a6c4-440a-951c-6afe080d41f4");
        LOG.info("Domain: " + domain);
    }


}
