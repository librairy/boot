/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import com.google.common.base.Strings;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Part;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.storage.dao.ItemsDao;
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
public class ItemsDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ItemsDaoTest.class);

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    EventBus eventBus;

    @Autowired
    PartsDao partsDao;

    @Test
    public void updateItems() throws DataNotFound {

        boolean stop = false;

        Optional<String> lastUri = Optional.empty();
        Integer splitSize = 20;
        AtomicInteger counter = new AtomicInteger(0);
        Integer maxCounter = 2;
        while(!stop){
            List<Item> items = itemsDao.list(splitSize, lastUri, false);
            items.stream().forEach( item ->{
                LOG.info("["+counter.incrementAndGet() + "] Item: "  + item);
                Resource resource = new Resource();
                resource.setUri(item.getUri());
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
            lastUri = Optional.of(URIGenerator.retrieveId(items.get(splitSize-1).getUri()));
        }
    }


    @Test
    public void listParts() throws DataNotFound {

        String itemUri   = "http://librairy.org/items/rL1yAYqh5co";

        Optional<String> offset = Optional.empty();
        Boolean completed = false;
        while(!completed){

            List<Part> parts = itemsDao.listParts(itemUri,10,offset, false);

            for (Part part : parts){
                LOG.info("adding: " + part.getUri() + " to domain");
            }

            if (parts.isEmpty() || parts.size() < 10) break;

            String lastUri = parts.get(parts.size()-1).getUri();
            offset = Optional.of(URIGenerator.retrieveId(lastUri));
        }

    }


    @Test
    public void ratioReport() throws DataNotFound {

        Double wordsPerItem = 0.0;
        Double wordsPerPart = 0.0;
        Double wordsPerResource = 0.0;

        int windowsize = 10;

        Optional<String> offset = Optional.empty();
        Boolean completed = false;
        while(!completed){


            List<Item> items = itemsDao.list(10, offset, false);

            for (Item item : items){


                LOG.info("checking '" + item.getUri() + " '");

                Item book = itemsDao.get(item.getUri(), true);
                if (Strings.isNullOrEmpty(book.getContent())) {
                    LOG.error("Book empty: " + book.getUri());
                    continue;
                }
                int bookWords = book.getContent().split(" ").length;

                // words per item
                if (wordsPerItem == 0.0){
                    wordsPerItem = Double.valueOf(bookWords);
                }

                wordsPerItem = (wordsPerItem + bookWords) / 2.0;

                // words per resource
                if (wordsPerResource == 0.0){
                    wordsPerResource = Double.valueOf(bookWords);
                }

                wordsPerResource = (wordsPerResource + bookWords) / 2.0;

                Optional<String> partOffset = Optional.empty();
                Boolean partsCompleted = false;

                while(!partsCompleted){
                    List<Part> parts = itemsDao.listParts(item.getUri(), windowsize, partOffset, false);

                    for (Part uri: parts){
                        LOG.debug("checking '" + uri + " '");


                        Part part = partsDao.get(uri.getUri(), true);
                        if (Strings.isNullOrEmpty(part.getContent())) {
                            LOG.error("Part empty: " + uri);
                            continue;
                        }
                        int chapterWords = part.getContent().split(" ").length;

                        // words per part
                        if (wordsPerPart == 0.0){
                            wordsPerPart = Double.valueOf(chapterWords);
                        }

                        wordsPerPart = (wordsPerPart + chapterWords) / 2.0;


                        // words per resource
                        if (wordsPerResource == 0.0){
                            wordsPerResource = Double.valueOf(chapterWords);
                        }

                        wordsPerResource = (wordsPerResource + chapterWords) / 2.0;

                    }

                    if (parts.isEmpty() || parts.size() < windowsize) break;

                    String lastUri = parts.get(parts.size()-1).getUri();
                    partOffset = Optional.of(URIGenerator.retrieveId(lastUri));

                }

            }

            if (items.isEmpty() || items.size() < windowsize) break;

            String lastUri = items.get(items.size()-1).getUri();
            offset = Optional.of(URIGenerator.retrieveId(lastUri));
        }


        LOG.info("Words per book: " + wordsPerItem);
        LOG.info("Words per part: " + wordsPerPart);
        LOG.info("Words per resource: " + wordsPerResource);

    }

}
