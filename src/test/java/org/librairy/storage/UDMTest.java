package org.librairy.storage;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import es.cbadenes.lab.test.IntegrationTest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.librairy.Config;
import org.librairy.model.Event;
import org.librairy.model.domain.LinkableElement;
import org.librairy.model.domain.relations.Bundles;
import org.librairy.model.domain.relations.HypernymOf;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Document;
import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Source;
import org.librairy.model.modules.BindingKey;
import org.librairy.model.modules.EventBus;
import org.librairy.model.modules.EventBusSubscriber;
import org.librairy.storage.actions.CountResourceAction;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.document.domain.WordDocument;
import org.librairy.storage.system.document.repository.WordDocumentRepository;
import org.librairy.storage.system.graph.repository.edges.DealsWithFromDocumentEdgeRepository;
import org.librairy.storage.system.graph.repository.edges.SimilarToEdgeRepository;
import org.librairy.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.SourceGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.cassandra.contactpoints = wiig.dia.fi.upm.es",
        "librairy.cassandra.port = 5011",
        "librairy.cassandra.keyspace = research",
        "librairy.elasticsearch.contactpoints = wiig.dia.fi.upm.es",
        "librairy.elasticsearch.port = 5021",
        "librairy.neo4j.contactpoints = wiig.dia.fi.upm.es",
        "librairy.neo4j.port = 5030",
        "librairy.eventbus.host = localhost",
        "librairy.eventbus.port=5041"})
public class UDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMTest.class);

    @Autowired
    UDM udm;

    @Autowired
    Helper helper;

    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    WordDocumentRepository wordDocumentRepository;

    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    DealsWithFromDocumentEdgeRepository dealsWithFromDocumentEdgeRepository;

    @Autowired
    EventBus eventBus;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    UnifiedNodeGraphRepositoryFactory factory;


    @Autowired
    SimilarToEdgeRepository similarToEdgeRepository;


    @Test
    public void publish(){
//        Document document = Resource.newDocument();
//        document.setUri("http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e");
//        eventBus.post(Event.from(document),RoutingKey.of(Resource.Type.DOCUMENT,Resource.State.CREATED));


//        Topic topic = Resource.newTopic();
//        topic.setUri("http://drinventor.eu/topics/7ae8a17e15a9b7aec4e58bc42543c5bd");
//        eventBus.post(Event.from(topic),RoutingKey.of(Resource.Type.TOPIC, Resource.State.CREATED));


        // For Comparator
        org.librairy.model.domain.resources.Analysis analysis = org.librairy.model.domain.resources.Resource.newAnalysis();
        analysis.setType("topic-model");
        //analysis.setDescription("item");
        analysis.setDescription("part");
        analysis.setDomain("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499");
        eventBus.post(Event.from(analysis), org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.ANALYSIS, org.librairy.model.domain.resources.Resource.State.CREATED));


        // For Modeler
//        Contains relation = Relation.newContains("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499", "http://drinventor.eu/documents/c369c917fecf3b4828688bdb6677dd6e");
//        eventBus.post(Event.from(relation), RoutingKey.of(Relation.Type.CONTAINS,Relation.State.CREATED));

    }

    @Test
    public void summary(){
        Arrays.stream(org.librairy.model.domain.resources.Resource.Type.values()).forEach(type -> System.out.println(">"+type.name() + ": " + udm.find(type).all().size() ));
        Arrays.stream(Relation.Type.values()).forEach(type -> System.out.println(">"+type.name() + ": " + udm.find(type).all().size() ));
    }

    @Test
    public void fixModel(){


        // Remove 'd' term
        List<String> uri = udm.find(org.librairy.model.domain.resources.Resource.Type.TERM).by(org.librairy.model.domain.resources.Term.CONTENT, "t");
        System.out.println(uri);

        udm.delete(org.librairy.model.domain.resources.Resource.Type.TERM).byUri(uri.get(0));

//        udm.delete(Resource.Type.TOPIC).all();
//        udm.delete(Resource.Type.WORD).all();
//        udm.delete(Resource.Type.TERM).all();


//        Domain domain = Resource.newDomain();
//        domain.setUri("http://drinventor.eu/domains/7df34748-7fad-486e-a799-3bcd86a03499");
//        domain.setName("siggraph");
//
//        System.out.println(udm.find(Resource.Type.DOMAIN).in(Resource.Type.SOURCE,"http://drinventor.eu/sources/00729c4c-f449-40d5-ae83-482278e83e9a"));
//
//        System.out.println(udm.find(Resource.Type.DOMAIN).all());
//
//

//        System.out.println("adding documents to domain...");
//        udm.find(Resource.Type.DOCUMENT).all().forEach(uri -> udm.save(Relation.newContains(domain.getUri(),uri)));

//        List<String> items = udm.find(Resource.Type.ITEM).all();
//        System.out.println("Total Items: " + items.size());
//        List<String> itemsInDomain = udm.find(Resource.Type.ITEM).in(Resource.Type.DOMAIN, domain.getUri());
//        System.out.println("Items in Domain: " + itemsInDomain.size());



//        System.out.println("adding items to document to domain...");
//        udm.find(Resource.Type.DOCUMENT).all().forEach(uri -> udm.save(Relation.newContains(domain.getUri(),uri)));


    }


    @Test
    public void read() throws InterruptedException {


//        List<String> docs = udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOMAIN, "http://librairy" +
//                ".org/domains/default");
//
//        System.out.println("Found: " + docs.size() + " documents");

//        List<String> sources = udm.find(Resource.Type.SOURCE).all();
//
//        List<String> itemUris = udm.find(Resource.Type.ITEM).from(Resource.Type.SOURCE, sources.get(0));
//
//        System.out.println(itemUris);


//        Iterator<Map<String, Object>> result = udm.queryGraph("match (d:Document) return count " +
//                "(d)");
//
//        System.out.println(result);


//        Select select = QueryBuilder.select().from("research", "items");
//        select.where(QueryBuilder.eq("uri","http://librairy.org/items/297f44cc83545ea4d9ce5ceb0d8aadb2"));
//
//        List<LinkableElement> result = udm.queryColumn(select);
//
//        System.out.println(result);


//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
//                .withQuery(QueryBuilders.termQuery("content", "communication"))
//                .withHighlightFields(new HighlightBuilder.Field("message"))
//                .withPageable(new PageRequest(0, 10))
//                .build();
//
//        List<String> result = udm.queryDocument(searchQuery);
//
//        System.out.println(result);


        Iterable<Relation> res = helper.getUnifiedColumnRepository().findBy(Relation.Type.BUNDLES, "item",
                "http://librairy" +
                        ".org/items/297f44cc83545ea4d9ce5ceb0d8aadb2");


        List<String> doc = udm.find(Resource.Type.DOCUMENT).from(Resource.Type.ITEM, "http://librairy" +
                ".org/items/297f44cc83545ea4d9ce5ceb0d8aadb2");


        System.out.println(doc);


//        Boolean res = helper.getUnifiedDocumentRepository().exists(Resource.Type.ITEM, "http://librairy" +
//                ".org/items/297f44cc83545ea4d9ce5ceb0d8aadb3");

        long documents = 0;
        double docRate = 0.0;

        long items = 0;
        double itemRate = 0.0;

        long delay = 5000l;
        while(true){

            long newDocs                = countFor(Resource.Type.DOCUMENT);
            long incrementDocs          = newDocs - documents;
            Double rateDocs             = Double.valueOf(incrementDocs) / Double.valueOf(delay/1000);
            docRate = (docRate + rateDocs) /2.0;
            LOG.info("Docs creation. [instant: " + rateDocs  + " docs/sec] [mean: " + docRate + " docs/sec]");
            documents = newDocs;

            long newItems           = countFor(Resource.Type.ITEM);
            long incrementItems     = newItems - items;

            Double rateItems        = Double.valueOf(incrementItems) / Double.valueOf(delay/1000);
            itemRate = (itemRate + rateItems) /2.0;
            LOG.info("Items creation. [instant: " + rateItems  + " items/sec] [mean: " + itemRate + " items/sec]");
            items = newItems;

//            long docs = udm.count(Resource.Type.DOCUMENT).all();
//            long items = udm.count(Resource.Type.ITEM).all();
//            System.out.println("Node Items: " + items);

            Thread.sleep(delay);
        }
    }

    private long countFor(Resource.Type type){
//        long dDocs = helper.getUnifiedDocumentRepository().count(type);
//        long cDocs = helper.getUnifiedColumnRepository().count(type);
        long n1Docs = helper.getTemplateFactory().of(type).countAll();
//        long n2Docs = helper.getUnifiedNodeGraphRepository().count(type);
//        LOG.info(type.name() + "s from [ElasticSearch=" + dDocs+"] [Cassandra="+cDocs+"] [Neo4j(template)" +
//                "="+n1Docs+"] [Neo4j(repository)="+n2Docs+"]");
        LOG.info(type.name() + "s from [[Neo4j(template)" +
                "="+n1Docs+"]");
        return n1Docs;
    }

    @Test
    public void purge(){
        udm.delete(Resource.Type.ANY).all();
        udm.delete(Relation.Type.ANY).all();
    }


    @Test
    public void relations(){

        _saveRelation(Relation.newAggregates("u1","u2"));
        _saveRelation(Relation.newAppearedIn("u1","u2"));
        _saveRelation(Relation.newBundles("u1","u2"));
        _saveRelation(Relation.newComposes("u1","u2"));
        _saveRelation(Relation.newContains("u1","u2"));
        _saveRelation(Relation.newContains("u1","u2"));
        _saveRelation(Relation.newDealsWithFromDocument("u1","u2"));
        _saveRelation(Relation.newDealsWithFromItem("i1","i2"));
        _saveRelation(Relation.newDealsWithFromPart("p1","p2"));
        _saveRelation(Relation.newDescribes("u1","u2"));
        _saveRelation(Relation.newEmbeddedIn("u1","u2"));
        _saveRelation(Relation.newEmergesIn("u1","u2"));
        _saveRelation(Relation.newHypernymOf("u1","u2"));
        _saveRelation(Relation.newMentionsFromTerm("u1","u2"));
        _saveRelation(Relation.newMentionsFromTopic("t1","t2"));
        _saveRelation(Relation.newPairsWith("u1","u2"));
        _saveRelation(Relation.newProvides("u1","u2"));
    }

    private void _saveRelation(Relation relation){
        LOG.info("Saving: " + relation.getType() + " ...");
        relation.setUri(UUID.randomUUID().toString());
        helper.getUnifiedColumnRepository().save(relation);
        LOG.info("Saved: " + relation.getType());
    }


    @Test
    public void save(){


        List<Relation> relations = udm.find(Relation.Type.BUNDLES).all();

        relations.stream().parallel().forEach(relation -> {
            LOG.info("saving relation: " + relation.getUri());
            helper.getUnifiedColumnRepository().save(relation);
        });







//        String document = "http://librairy.org/documents/07043092";
//        String item     = "http://librairy.org/items/b4e1576bf9fb01271e78c52947b12644";
//        Bundles bundle = Relation.newBundles(document,item);
//        udm.save(bundle);


//        Document doc = Resource.newDocument();
//        doc.setUri("http://librairy.org/documents/06856810");
//        doc.setCreationTime("2016-04-12T16:26+0000");
//        udm.save(doc);


    }



    @Test

    public void findFrom(){
        String uri = "http://drinventor.eu/items/a5179367d5ebf825f01d9247dacae66";
        List<String> domains = udm.find(org.librairy.model.domain.resources.Resource.Type.DOMAIN).from(org.librairy.model.domain.resources.Resource.Type.ITEM, uri);
        System.out.println("Domains: " + domains);
    }


    @Test
    public void hypernym(){

        HypernymOf hypernym = Relation.newHypernymOf("http://librairy.org/terms/3324e10e-87c5-49a5-a9ba-c79adf3beba0", "http://librairy.org/terms/4cbd8d67-05d1-4d0b-b1da-7ccd3244298a");
        hypernym.setDomain("http://librairy.org/domains/28cd53dc-bc1c-417d-9ae5-2b5a7052d819");
        hypernym.setWeight(0.04766949152542373);
        udm.save(hypernym);

        assert true;
    }


    @Test
    public void saveSource(){
        AtomicInteger counter = new AtomicInteger(0);

        eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public void handle(Event event) {
                LOG.info("Handle Event: " + event);
                counter.incrementAndGet();
            }
        }, BindingKey.of(org.librairy.model.modules.RoutingKey.of(org.librairy.model.domain.resources.Resource.Type.SOURCE, org.librairy.model.domain.resources.Resource.State.CREATED),"test"));


        org.librairy.model.domain.resources.Source source = org.librairy.model.domain.resources.Resource.newSource();
        source.setUri("http://librairy.org/sources/0b3e80ae-d598-4dd4-8c54-38e2229f0bf8");
        source.setUrl("file://opt/librairy/inbox/upm");
        source.setName("test-source");
        source.setProtocol("file");
        source.setCreationTime("20160101T22:02");
        source.setDescription("testing purposes");

        LOG.info("Saving source: " + source);
        udm.save(source);
        LOG.info("source saved!");

        Optional<org.librairy.model.domain.resources.Resource> source2 = udm.read(org.librairy.model.domain.resources.Resource.Type.SOURCE).byUri(source.getUri());
        Assert.assertTrue(source2.isPresent());
        Assert.assertEquals(source.getUri(),source2.get().getUri());
        Assert.assertEquals(source.getName(),source2.get().asSource().getName());

        LOG.info("Deleting source: " + source);
        udm.delete(org.librairy.model.domain.resources.Resource.Type.SOURCE).byUri(source.getUri());
        LOG.info("source deleted!");

        Optional<org.librairy.model.domain.resources.Resource> source3 = udm.read(org.librairy.model.domain.resources.Resource.Type.SOURCE).byUri(source.getUri());
        Assert.assertFalse(source3.isPresent());

    }

    @Test
    public void findDocumentsInDomain(){
        // Source
        org.librairy.model.domain.resources.Source source = org.librairy.model.domain.resources.Resource.newSource();
        udm.save(source);

        // Domain
        org.librairy.model.domain.resources.Domain domain = org.librairy.model.domain.resources.Resource.newDomain();
        udm.save(domain);

        // Document 1
        org.librairy.model.domain.resources.Document doc1 = org.librairy.model.domain.resources.Resource.newDocument();
        udm.save(doc1);
        // -> document1 in source
        udm.save(Relation.newProvides(source.getUri(),doc1.getUri()));
        // -> document1 in domain
        udm.save(Relation.newContains(domain.getUri(),doc1.getUri()));

        // Document 2
        org.librairy.model.domain.resources.Document doc2 = org.librairy.model.domain.resources.Resource.newDocument();
        udm.save(doc2);
        // -> document2 in source
        udm.save(Relation.newProvides(source.getUri(),doc2.getUri()));
        // -> document2 in domain
        udm.save(Relation.newContains(domain.getUri(),doc2.getUri()));

        // Getting Documents
        List<String> documents = udm.find(org.librairy.model.domain.resources.Resource.Type.DOCUMENT).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain.getUri());

        // Delete
        udm.delete(org.librairy.model.domain.resources.Resource.Type.SOURCE).byUri(source.getUri());
        udm.delete(org.librairy.model.domain.resources.Resource.Type.DOMAIN).byUri(domain.getUri());
        udm.delete(org.librairy.model.domain.resources.Resource.Type.DOCUMENT).byUri(doc1.getUri());
        udm.delete(org.librairy.model.domain.resources.Resource.Type.DOCUMENT).byUri(doc2.getUri());

        Assert.assertTrue(documents != null);
        Assert.assertEquals(2,documents.size());
    }


    @Test
    public void getTopicDistributionOfDocumentsInDomain(){

        String domain = "http://librairy.org/domains/72dba453-eaba-4cb6-99f3-a456e96f3768";

        Iterable<Relation> relations = udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domain);
        LOG.info("Result: " + relations);

    }

    @Test
    public void deleteAndCreateEmbeddedINRelations(){

        try {
            String domain = "http://librairy.org/domains/d4a5f93d-fc90-453e-a2d5-7ca27dfb4e29";
            String word = "http://librairy.org/words/67f76420-4d11-42d5-a692-f2bd5c353ac9";

            LOG.info("First loop");
            udm.delete(Relation.Type.EMBEDDED_IN).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain);


            Iterable<Relation> pairs = udm.find(Relation.Type.PAIRS_WITH).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domain);
            if (pairs != null){
                for (Relation pair : pairs) {
                    udm.delete(Relation.Type.PAIRS_WITH).byUri(pair.getUri());
                }
            }

            udm.save(Relation.newEmbeddedIn(word,domain));


            LOG.info("Second loop");
            udm.delete(Relation.Type.EMBEDDED_IN).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain);
            udm.delete(Relation.Type.PAIRS_WITH).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain);
            udm.save(Relation.newEmbeddedIn(word,domain));

            LOG.info("Third loop");
            udm.delete(Relation.Type.EMBEDDED_IN).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain);
            udm.delete(Relation.Type.PAIRS_WITH).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain);
            udm.save(Relation.newEmbeddedIn(word,domain));


        }catch (Exception e){
            LOG.error("Error",e);
        }
    }

    public void deleteAll(){
        udm.delete(org.librairy.model.domain.resources.Resource.Type.ANY).all();
    }


    @Test
    public void findWordsInDomains(){

        List<String> domains = Arrays.asList(new String[]{
                "http://librairy.org/domains/e19f1196-9233-41be-9eb0-f3ee5895998d",
                "http://librairy.org/domains/031a7709-17d0-4b53-9982-4feac9281082",
                "http://librairy.org/domains/6be14ee9-72ac-4c35-868f-9735851b1042",
                "http://librairy.org/domains/cb0b34ba-9401-48da-8a7a-c435361516a4"
        });

        domains.forEach(domain -> {
            List<String> wordsByDomain = udm.find(org.librairy.model.domain.resources.Resource.Type.WORD).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domain);
            LOG.info("Domain '" + domain + "' contains: " + wordsByDomain.size() + " words");
        });


        List<String> words = udm.find(org.librairy.model.domain.resources.Resource.Type.WORD).all();
        LOG.info("Total words: " + words.size());

    }

    @Test
    public void readADomain(){

        Optional<org.librairy.model.domain.resources.Resource>  domain = udm.read(org.librairy.model.domain.resources.Resource.Type.DOMAIN).byUri("http://librairy.org/domains/4830d78e-a6c4-440a-951c-6afe080d41f4");
        LOG.info("Domain: " + domain);
    }


    @Test
    public void findWord (){

        String wordURI = "http://librairy.org/words/f643a3b3-16ce-4158-b4f8-0f82c53092bc";
        String domainURI ="http://librairy.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5";

        WordDocument doc = wordDocumentRepository.findOne(wordURI);
        System.out.println(doc);
    }

}
