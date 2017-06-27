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
import org.librairy.boot.storage.dao.CounterDao;
import org.librairy.boot.storage.dao.ParametersDao;
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
public class CountersDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParametersDao.class);

    @Autowired
    CounterDao dao;


    @Autowired
    UDM udm;

    @Before
    @After
    public void setup(){
        Arrays.stream(Resource.Type.values()).forEach(type -> udm.delete(type).all());
    }

    @Test
    public void crud(){

        String counter = "counter.test";

        Assert.assertEquals(Long.valueOf(0), dao.getValue(counter));

        Assert.assertTrue(dao.increment(counter));

        Assert.assertEquals(Long.valueOf(1), dao.getValue(counter));

        Assert.assertTrue(dao.increment(counter, 10l));

        Assert.assertEquals(Long.valueOf(11), dao.getValue(counter));

        Assert.assertTrue(dao.decrement(counter));

        Assert.assertEquals(Long.valueOf(10), dao.getValue(counter));

        Assert.assertTrue(dao.decrement(counter,5l));

        Assert.assertEquals(Long.valueOf(5), dao.getValue(counter));

        Assert.assertTrue(dao.reset(counter));

        Assert.assertEquals(Long.valueOf(0), dao.getValue(counter));

        Assert.assertTrue(dao.increment(counter,100l));
        Assert.assertEquals(Long.valueOf(100), dao.getValue(counter));

        Assert.assertTrue(dao.truncate());
        Assert.assertEquals(Long.valueOf(0), dao.getValue(counter));

    }

    @Test
    public void crudByDomain(){
        String domainUri = URIGenerator.fromId(Resource.Type.DOMAIN, "sample");

        String counter = "counter.test";

        Assert.assertEquals(Long.valueOf(0), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.increment(domainUri, counter));

        Assert.assertEquals(Long.valueOf(1), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.increment(domainUri, counter, 10l));

        Assert.assertEquals(Long.valueOf(11), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.decrement(domainUri, counter));

        Assert.assertEquals(Long.valueOf(10), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.decrement(domainUri, counter,5l));

        Assert.assertEquals(Long.valueOf(5), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.reset(domainUri, counter));

        Assert.assertEquals(Long.valueOf(0), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.increment(domainUri, counter,100l));
        Assert.assertEquals(Long.valueOf(100), dao.getValue(domainUri, counter));

        Assert.assertTrue(dao.truncate(domainUri));
        Assert.assertEquals(Long.valueOf(0), dao.getValue(domainUri, counter));

    }
}
