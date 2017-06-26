/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import com.google.common.base.Strings;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Listener;
import org.librairy.boot.model.domain.resources.Part;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.dao.ItemsDao;
import org.librairy.boot.storage.dao.ListenersDao;
import org.librairy.boot.storage.dao.PartsDao;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoTest.class)
public class ListenersDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ListenersDaoTest.class);

    @Autowired
    ListenersDao dao;


    @Autowired
    UDM udm;

    @Test
    public void crudTest() throws DataNotFound {


        Assert.assertTrue(dao.list(10l, Optional.empty(), false).isEmpty());

        Assert.assertTrue(udm.find(Resource.Type.LISTENER).all().isEmpty());


        Listener listener = new Listener();
        listener.setRoute("items.created");
        listener.setUri(URIGenerator.fromId(Resource.Type.LISTENER, listener.getRoute()));
        listener.setCreationTime(TimeUtils.asISO());
        udm.save(listener);

        Assert.assertFalse(dao.list(10l, Optional.empty(), false).isEmpty());

        Assert.assertFalse(udm.find(Resource.Type.LISTENER).all().isEmpty());

        Optional<Resource> result = udm.read(Resource.Type.LISTENER).byUri(listener.getUri());


        Assert.assertTrue(result.isPresent());

        Assert.assertEquals(listener, result.get().asListener());

        List<Listener> resultByRoute = dao.getByRoute(listener.getRoute());

        Assert.assertFalse(resultByRoute.isEmpty());

        Assert.assertTrue(resultByRoute.size() == 1);

        Assert.assertEquals(listener, resultByRoute.get(0));

        dao.removeByRoute(listener.getRoute());

        Assert.assertTrue(dao.list(10l, Optional.empty(), false).isEmpty());

        Assert.assertTrue(udm.find(Resource.Type.LISTENER).all().isEmpty());


    }




}
