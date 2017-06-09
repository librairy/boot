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


    @Test
    public void saveTest() throws DataNotFound {

        List<Listener> res1 = dao.list(10, Optional.empty(), false);
        Assert.assertTrue(res1.isEmpty());

        Listener res2 = dao.get("sample");
        Assert.assertTrue(Strings.isNullOrEmpty(res2.getId()));

        Boolean res3 = dao.save("documents.created");
        Assert.assertTrue(res3);

        List<Listener> res4 = dao.list(10, Optional.empty(), false);
        Assert.assertTrue(res1.isEmpty());

    }




}
