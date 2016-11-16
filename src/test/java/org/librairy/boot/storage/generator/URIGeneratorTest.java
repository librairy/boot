/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.generator;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.Config;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.uri= drinventor.eu"
})
public class URIGeneratorTest {

    @Autowired
    URIGenerator uriGenerator;

    @Test
    public void librairy(){
        Assert.assertEquals("librairy.org", uriGenerator.domainUri);
        Assert.assertEquals("http://librairy.org/", uriGenerator.base);

        Assert.assertEquals("http://librairy.org/documents/123", uriGenerator.from(Resource.Type.DOCUMENT,"123"));

    }

    @Test
    public void drinventor(){
        Assert.assertEquals("drinventor.eu", uriGenerator.domainUri);
        Assert.assertEquals("http://drinventor.eu/", uriGenerator.base);

        Assert.assertEquals("http://drinventor.eu/documents/123", uriGenerator.from(Resource.Type.DOCUMENT,"123"));

    }
}
