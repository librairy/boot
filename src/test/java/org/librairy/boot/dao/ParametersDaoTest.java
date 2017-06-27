/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.domain.resources.Param;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.dao.ParametersDao;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoTest.class)
public class ParametersDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParametersDao.class);

    @Autowired
    ParametersDao dao;

    @Autowired
    UDM udm;

    @Before
    @After
    public void setup(){
        Arrays.stream(Resource.Type.values()).forEach( type -> udm.delete(type).all());
    }

    @Test
    public void crud(){

        String domainUri = URIGenerator.fromId(Resource.Type.DOMAIN, "sample");

        Assert.assertFalse(dao.get(domainUri, "lda.delay").isPresent());

        // Initialize a domain
        dao.initialize(domainUri);

        Assert.assertTrue(dao.get(domainUri, "lda.delay").isPresent());

        List<Param> parameters = dao.listAt(domainUri);
        Assert.assertFalse(parameters.isEmpty());

        dao.remove(domainUri,"lda.delay");

        Assert.assertFalse(dao.get(domainUri, "lda.delay").isPresent());

        List<Param> parameters2 = dao.listAt(domainUri);
        Assert.assertFalse(parameters2.isEmpty());
        Assert.assertTrue(parameters2.size() < parameters.size());

        dao.remove(domainUri);

        Assert.assertFalse(dao.get(domainUri, "lda.delay").isPresent());

        List<Param> parameters3 = dao.listAt(domainUri);
        Assert.assertTrue(parameters3.isEmpty());

    }
}
