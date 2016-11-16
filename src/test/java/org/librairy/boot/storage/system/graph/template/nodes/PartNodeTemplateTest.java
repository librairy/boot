/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.template.nodes;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.system.graph.GraphConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.StreamSupport;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = {
        "librairy.graphdb.host = librairy.linkeddata.es",
        "librairy.graphdb.port = 7474",
        "librairy.uri = librairy.org"
})
public class PartNodeTemplateTest {


    private static final Logger LOG = LoggerFactory.getLogger(PartNodeTemplateTest.class);

    @Autowired
    PartNodeTemplate nodeTemplate;


    @Test
    public void readAllInDomain(){

        String domainUri = "http://librairy.org/domains/default";


        LOG.info("reading all parts in domain: " + domainUri);

        Iterable<Resource> parts = nodeTemplate.findFromIterable(Resource.Type.DOMAIN, domainUri);

        LOG.info("parts read!");

        long num = StreamSupport.stream(parts.spliterator(), false).count();

        LOG.info(num + " parts!");

    }
}
