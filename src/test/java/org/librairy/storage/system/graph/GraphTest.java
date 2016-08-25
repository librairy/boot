package org.librairy.storage.system.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.librairy.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.librairy.storage.system.graph.template.edges.SimilarDocEdgeTemplate;
import org.librairy.storage.system.graph.template.edges.SimilarItemEdgeTemplate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.storage.system.graph.repository.edges.BundlesEdgeRepository;
import org.librairy.storage.system.graph.repository.edges.SimilarToEdgeRepository;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = {
        "librairy.columndb.host = 192.168.99.100",
        "librairy.documentdb.host = 192.168.99.100",
        "librairy.graphdb.host = 192.168.99.100",
        "librairy.eventbus.host = 192.168.99.100"
})
public class GraphTest {

    private static final Logger LOG = LoggerFactory.getLogger(GraphTest.class);


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
    public void findSimilar(){

        String startDocUri = "http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e";
        String endDocUri   = "http://drinventor.eu/documents/f6f36164961229eac1bf19431a3744a0";

        String startItemUri = "http://drinventor.eu/items/c369c917fecf3b4828688bdb6677dd6e";
        String endItemUri   = "http://drinventor.eu/items/715f6df41fdf75cb3d0db7fce050f301";

//        Neo4jTemplate template = new Neo4jTemplate(session);
//        Map<String,String> params = new HashMap<>();
//        params.put("0","http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e");
//        params.put("1","http://drinventor.eu/documents/f6f36164961229eac1bf19431a3744a0");
//        Result result = template.query("match (node1:Document{uri:{0}})-[r:SIMILAR_TO]-(node2:Document{uri:{1}}) return r", params);
//        System.out.println("Query Results: "+ result.queryResults());
//
//        params.put("0","http://drinventor.eu/items/c369c917fecf3b4828688bdb6677dd6e");
//        params.put("1","http://drinventor.eu/items/715f6df41fdf75cb3d0db7fce050f301");
//        Result result2 = template.query("match (node1:Item{uri:{0}})-[r:SIMILAR_TO]-(node2:Item{uri:{1}}) return r", params);
//        System.out.println("Query Results: "+ result2.queryResults());


        System.out.println("0->" + similarDocGraphQuery.findOne(startDocUri, endDocUri));
        System.out.println("1->" + similarItemGraphQuery.findOne(startItemUri, endItemUri));

        try{
//            Iterable<SimilarToDocumentsEdge> result = similarToDocumentsEdgeRepository.giveme(startUri, endUri);
//            System.out.println("1->" + result);
//            System.out.println("1->" + similarToItemsEdgeRepository.findByNodes(startItemUri,endItemUri));
//            System.out.println("2->" + similarToDocumentsEdgeRepository.findByNodes(startDocUri, endDocUri));
//            System.out.println("2->" + similarToEdgeRepository.findItemsByNodes(startItemUri,endItemUri));

//            System.out.println("3->" + similarToEdgeRepository.findDocumentsByNodes(startDocUri,endDocUri));
//            System.out.println("3->" + similarToEdgeRepository.findItemsByNodes(startItemUri,endItemUri));

//            Iterable<DocumentNode> result2 = documentGraphRepository.findByDocument(startUri);
//            System.out.println(result2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
