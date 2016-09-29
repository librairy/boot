/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.storage.system.graph.repository.edges.BundlesEdgeRepository;
import org.librairy.storage.system.graph.repository.edges.SimilarToEdgeRepository;
import org.librairy.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.librairy.storage.system.graph.template.edges.SimilarDocEdgeTemplate;
import org.librairy.storage.system.graph.template.edges.SimilarItemEdgeTemplate;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.ogm.session.Session;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
public class BatchGraphTest {

    private static final Logger LOG = LoggerFactory.getLogger(BatchGraphTest.class);


    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    SimilarToEdgeRepository similarToEdgeRepository;

    @Autowired
    BundlesEdgeRepository bundlesEdgeRepository;

    @Autowired
    Session session;

    @Autowired
    TemplateExecutor queryExecutor;

//    @Autowired
//    Neo4jTemplate template;


    @Autowired
    SimilarDocEdgeTemplate similarDocGraphQuery;

    @Autowired
    SimilarItemEdgeTemplate similarItemGraphQuery;


    @Test
    public void saveSimilarTo(){

        Map<String, String> config = new HashMap<>();
        config.put( "dbms.pagecache.memory", "512m" );
        BatchInserter inserter = BatchInserters.inserter( "target/neo4jdb-batchinsert" , config);

//        BatchInserterIndexProvider indexProvider = new LuceneBatchInserterIndexProvider( inserter );
//
//        BatchInserterIndex actors = indexProvider.nodeIndex( "actors", MapUtil.stringMap( "type", "exact" ) );
//        actors.setCacheCapacity( "name", 100000 );
//
//        Map<String, Object> properties = MapUtil.map( "name", "Keanu Reeves" );
//        long node = inserter.createNode( properties );
//        actors.add( node, properties );
//
//        //make the changes visible for reading, use this sparsely, requires IO!
//        actors.flush();
//
//        // Make sure to shut down the index provider as well
//        indexProvider.shutdown();
//        inserter.shutdown();


    }

}
