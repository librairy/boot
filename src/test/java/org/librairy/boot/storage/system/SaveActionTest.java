/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.Config;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.domain.resources.Source;
import org.librairy.boot.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created on 05/09/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class SaveActionTest {

    private static final Logger LOG = LoggerFactory.getLogger(SaveActionTest.class);

    @Autowired
    UDM udm;

    @Test
    public void saveSource(){

        Source source = Resource.newSource("sample");
        udm.save(source);

    }


    @Test
    public void readContainsRelations(){

        String domainUri = "http://librairy.org/domains/default";

        LOG.info("Getting docs from domain: " + domainUri);
        LOG.info(udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOMAIN, domainUri).size() + " found!");

        LOG.info("Getting items from domain: " + domainUri);
        LOG.info(udm.find(Resource.Type.ITEM).from(Resource.Type.DOMAIN, domainUri).size() + " found!");

        LOG.info("Getting parts from domain: " + domainUri);
        LOG.info(udm.find(Resource.Type.PART).from(Resource.Type.DOMAIN, domainUri).size() + " found!");


    }

    @Test
    public void saveItemContainsRelations(){

        String domainUri = "http://librairy.org/domains/default";

        udm.find(Resource.Type.ITEM).all().forEach(item -> udm.save(Relation.newContains(domainUri,item.getUri())));

    }


    @Test
    public void savePartContainsRelations(){

        String domainUri = "http://librairy.org/domains/default";

        udm.find(Resource.Type.PART).all().forEach(item -> udm.save(Relation.newContains(domainUri,item.getUri())));

    }

}
