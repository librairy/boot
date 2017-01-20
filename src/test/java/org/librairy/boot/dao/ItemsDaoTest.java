/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.storage.dao.ItemsDao;
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
public class ItemsDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsDaoTest.class);

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    EventBus eventBus;

    @Test
    public void updateItems() throws DataNotFound {

        boolean stop = false;

        Optional<String> lastUri = Optional.empty();
        Integer splitSize = 20;
        AtomicInteger counter = new AtomicInteger(0);
        Integer maxCounter = 2;
        while(!stop){
            List<String> items = itemsDao.listAll(splitSize, lastUri);
            items.stream().forEach( uri ->{
                LOG.info("["+counter.incrementAndGet() + "] Item: "  + uri);
                Resource resource = new Resource();
                resource.setUri(uri);
                eventBus.post(Event.from(resource), RoutingKey.of(Resource.Type.ITEM, Resource.State.CREATED));

//                // notifiy parts
//                itemsDao.listParts(uri, 100, Optional.empty()).stream().forEach(part -> {
////                    LOG.info("Part: "  + part);
//                    Resource partResource = new Resource();
//                    partResource.setUri(part);
//                    eventBus.post(Event.from(partResource), RoutingKey.of(Resource.Type.PART, Resource.State.CREATED));
//                });


            } );
            if (counter.get()>maxCounter) break;
            if (items.isEmpty() || items.size() < splitSize) break;
            lastUri = Optional.of(URIGenerator.retrieveId(items.get(splitSize-1)));
        }



    }

}
