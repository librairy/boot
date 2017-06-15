/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Annotation;
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.dao.AnnotationsDao;
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
public class AnnotationsDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationsDaoTest.class);

    @Autowired
    AnnotationsDao annotationsDao;

    @Autowired
    UDM udm;

    @Test
    public void createReadDelete() throws DataNotFound {

        Assert.assertTrue(udm.find(Resource.Type.ANNOTATION).all().isEmpty());

        Annotation annotation = new Annotation();
        annotation.setResource("http://librairy.org/items/1");
        annotation.setDescription("testing annotation");
        annotation.setPurpose("test");
        annotation.setLanguage("en");
        annotation.setScore(1.0);
        annotation.setCreator("junit-test");
        annotation.setFormat("text/plain");
        annotation.setType("lemma");
        annotation.setValue(ImmutableMap.of("content","lemma1 lemma2 lemma3"));
        annotation.setSelection(ImmutableMap.of("range","all"));
        udm.save(annotation);

        Assert.assertTrue(!udm.find(Resource.Type.ANNOTATION).all().isEmpty());

        Optional<Resource> resource = udm.read(Resource.Type.ANNOTATION).byUri(annotation.getUri());
        Assert.assertTrue(resource.isPresent());
        Assert.assertTrue(resource.get().asAnnotation().equals(annotation));


        List<Annotation> annotationsByResource = annotationsDao.getByResource(annotation.getResource(), Optional.empty(), Optional.empty(), Optional.empty());
        Assert.assertTrue(!annotationsByResource.isEmpty());
        Assert.assertTrue(annotationsByResource.get(0).equals(annotation));

        List<Annotation> annotationsByResourceAndType = annotationsDao.getByResource(annotation.getResource(), Optional.of(annotation.getType()), Optional.empty(), Optional.empty());
        Assert.assertTrue(!annotationsByResourceAndType.isEmpty());
        Assert.assertTrue(annotationsByResourceAndType.get(0).equals(annotation));

        List<Annotation> annotationsByResourceAndPurpose = annotationsDao.getByResource(annotation.getResource(), Optional.empty(), Optional.of(annotation.getPurpose()), Optional.empty());
        Assert.assertTrue(!annotationsByResourceAndPurpose.isEmpty());
        Assert.assertTrue(annotationsByResourceAndPurpose.get(0).equals(annotation));

        List<Annotation> annotationsByResourceAndCreator = annotationsDao.getByResource(annotation.getResource(), Optional.empty(), Optional.empty(), Optional.of(annotation.getCreator()));
        Assert.assertTrue(!annotationsByResourceAndCreator.isEmpty());
        Assert.assertTrue(annotationsByResourceAndCreator.get(0).equals(annotation));

        Assert.assertTrue(annotationsDao.removeByResource(annotation.getResource(), Optional.empty(), Optional.empty(), Optional.empty()));
        Assert.assertTrue(udm.find(Resource.Type.ANNOTATION).all().isEmpty());

        udm.save(annotation);
        Assert.assertTrue(!udm.find(Resource.Type.ANNOTATION).all().isEmpty());
        udm.delete(Resource.Type.ANNOTATION).byUri(annotation.getUri());
        Assert.assertTrue(udm.find(Resource.Type.ANNOTATION).all().isEmpty());

    }

}
