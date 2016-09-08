package org.librairy.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.apache.commons.beanutils.BeanUtils;
import org.librairy.Config;
import org.librairy.model.domain.relations.Relation;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.model.domain.relations.SimilarToItems;
import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Word;
import org.librairy.storage.executor.ParallelExecutor;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.column.domain.SimilarToColumn;
import org.librairy.storage.system.column.repository.UnifiedColumnRepository;
import org.librairy.storage.system.graph.domain.edges.SimilarToItemsEdge;
import org.librairy.storage.system.graph.template.TemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by cbadenes on 02/03/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ConsistencyTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConsistencyTest.class);

    int NUM_WORDS           = 10;
    int NUM_TERMS           = 10;
    int NUM_DOC_TOPICS      = 5;
    int NUM_ITEM_TOPICS     = 6;
    int NUM_PART_TOPICS     = 11;
    int NUM_DOCUMENTS       = 100;
    int NUM_ITEMS           = 1;
    int NUM_PARTS           = 8;


    String domainUri        = "http://drinventor.eu/domains/20160310154040441-7ebbd0a179a8f1a7c254245e8ddb1272";

    // Resources
    int numDocs     = NUM_DOCUMENTS;
    int numItems    = numDocs*NUM_ITEMS;
    int numParts    = numItems*NUM_PARTS;
    int numTopics   = NUM_DOC_TOPICS+NUM_ITEM_TOPICS+NUM_PART_TOPICS;
    int numTerms    = NUM_TERMS;
    int numWords    = NUM_WORDS;

    // Relations
    int numProvides         = numDocs;
    int numComposes         = 1;
    int numContains         = numDocs;
    int numBundles          = numItems;
    int numDescribes        = numParts;
    int numDealsWithDoc     = numDocs*NUM_DOC_TOPICS;
    int numDealsWithItem    = numItems*NUM_ITEM_TOPICS;
    int numDealsWithPart    = numParts*NUM_PART_TOPICS;
    int numEmergesIn        = numTopics;
    int numSimilarDoc       = numDocs*numDocs;
    int numSimilarItem      = numItems*numItems;
    int numSimilarPart      = numParts*numParts;
    int numMentionTopic     = numTopics*numWords;
    int numMentionTerm      = numTerms*numWords;
    int numEmbedded         = numWords;
    int numAppearedIn       = numTerms;
    int numHypernyms        = numTerms*numTerms;
    int numPairs            = numWords*numWords;


    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TemplateFactory factory;

    @Autowired
    UnifiedColumnRepository columnRepository;

    @Test
    public void deleteAll() throws InterruptedException {
        udm.delete(org.librairy.model.domain.resources.Resource.Type.ANY).all();
    }

    @Test
    public void findDomain(){


        List<Resource> domains = udm.find(Resource.Type.DOMAIN).from(Resource.Type.TOPIC, "http://librairy.org/topics/af4781f5f4e20aff9e07c2eafeb4c55b");

        LOG.info("Domains: " + domains);




    }

    @Test
    public void pairWord() throws InterruptedException {
        try{
            Word w1 = Resource.newWord("test1");
            udm.save(w1);

            Word w2 = Resource.newWord("test2");
            udm.save(w2);

            String domainUri = "http://librairy.org/domains/default";

            Domain domain = Resource.newDomain("d2");
            udm.save(domain);

            Relation pair = Relation.newPairsWith(w1.getUri(),w2.getUri(),domain.getUri());
            pair.setWeight(5.3);
            udm.save(pair);
        }catch (Exception e){
            LOG.error("error",e);
        }

    }

    @Test
    public void deleteSimilar(){
        String domainUri = "http://librairy.org/domains/default";
        List<Relation> result = udm.find(Relation.Type.SIMILAR_TO_ITEMS).from(Resource.Type.DOMAIN, domainUri);
        System.out.println(result.size());


        udm.find(Relation.Type.SIMILAR_TO_ITEMS).from(Resource.Type.DOMAIN, domainUri).parallelStream()
                .forEach(relation -> udm.delete(Relation.Type.SIMILAR_TO_ITEMS).byUri(relation.getUri()));

    }

    @Test
    public void validate(){

        int numSources = 1;
        int numDomains = 1;
        int numDocs = 99;

        Assert.assertEquals(numSources, udm.find(Resource.Type.SOURCE).all().size());

        Assert.assertEquals(numDomains, udm.find(Resource.Type.DOMAIN).all().size());
        Assert.assertEquals(numDomains*numSources, udm.find(Relation.Type.COMPOSES).all().size());
        Assert.assertEquals(numDocs, udm.find(Relation.Type.CONTAINS).all().size());

        Assert.assertEquals(numDocs, udm.find(Relation.Type.PROVIDES).all().size());
        Assert.assertEquals(numDocs, udm.find(Resource.Type.DOCUMENT).all().size());
        Assert.assertEquals(numDocs, udm.find(Relation.Type.BUNDLES).all().size());
        Assert.assertEquals(numDocs, udm.find(Resource.Type.ITEM).all().size());


        // Topics
        int numTopics = Double.valueOf(2*Math.sqrt(numDocs/2)).intValue();
        Assert.assertEquals(numTopics, udm.find(Resource.Type.TOPIC).all().size());
        Assert.assertEquals(numTopics, udm.find(Relation.Type.EMERGES_IN).all().size());
        Assert.assertEquals(numDocs*numTopics, udm.find(Relation.Type.DEALS_WITH_FROM_DOCUMENT).all().size());
        Assert.assertEquals(numDocs*numTopics, udm.find(Relation.Type.DEALS_WITH_FROM_ITEM).all().size());


        // SIMILAR_TO
        BigInteger fd = factorial(BigInteger.valueOf(numDocs));
        BigInteger fc = factorial(BigInteger.valueOf(2));
        BigInteger fs = factorial(BigInteger.valueOf(numDocs-2));

        int combinations = fd.divide(fc.multiply(fs)).intValue();
        Assert.assertEquals(combinations, udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).all().size());
        Assert.assertEquals(combinations, udm.find(Relation.Type.SIMILAR_TO_ITEMS).all().size());

        // WORDS
        int numWords = udm.find(Resource.Type.WORD).all().size();
        Assert.assertEquals(numWords, udm.find(Relation.Type.EMBEDDED_IN).all().size());

        int numMentions = udm.find(Relation.Type.MENTIONS_FROM_TOPIC).all().size();
        Assert.assertTrue(numMentions < numWords);

//        int numPairs = udm.find(Relation.Type.PAIRS_WITH).all().size();
//        Assert.assertTrue(numMentions < numPairs);

    }

    @Test
    public void findDuplicates() throws InterruptedException {
        try{
            List<Relation> rels = udm.find(Relation.Type.EMBEDDED_IN).all();

            Set<String> records = new HashSet<>();

            for (Relation relation: rels){
                String direct = relation.getStartUri()+relation.getEndUri();
                if (records.contains(direct)){
                    LOG.info("Duplicated direct: " +relation);
                }
                records.add(direct);
                String inverse = relation.getEndUri()+relation.getStartUri();
                if (records.contains(direct)){
                    LOG.info("Duplicated inverse: " +relation);
                }
                records.add(inverse);
            }

        }catch (Exception e){
            LOG.error("error",e);
        }
    }


    @Test
    public void findSimilar() throws InvocationTargetException, IllegalAccessException {
        String domainUri = "http://librairy.org/domains/default";
        LOG.info("reading all similar items..");
//        Iterable<Relation> rels = columnRepository.findBy(Relation.Type.SIMILAR_TO_ITEMS, "domain", domainUri);
//        LOG.info("all similar items read");
//        SimilarToColumn simColumRel = (SimilarToColumn) rels.iterator().next();
//        SimilarToItems rel = new SimilarToItems();
//        BeanUtils.copyProperties(rel,simColumRel);
//        LOG.info("trying to save on graph-db..");
//        factory.of(Relation.Type.SIMILAR_TO_ITEMS).save(rel);


        Instant start  = Instant.now();
        columnRepository.findBy(Relation.Type.SIMILAR_TO_ITEMS, "domain", domainUri).forEach(rel-> {

            if (!URIGenerator.typeFrom(rel.getStartUri()).equals(Resource.Type.ITEM)) return;

            SimilarToColumn columnRel = (SimilarToColumn) rel;
            SimilarToItems simRel = new SimilarToItems();
            try {
                BeanUtils.copyProperties(simRel,columnRel);
                factory.of(Relation.Type.SIMILAR_TO_ITEMS).save(simRel);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        Instant end  = Instant.now();
        LOG.info("Completed in: " + ChronoUnit.MINUTES.between(start,end) + "min " + (ChronoUnit.SECONDS.between(start,end)%60) + "secs");
    }

    @Test
    public void deleteTopics(){
        String domainUri = "http://librairy.org/domains/default";
        Iterable<Relation> rels = columnRepository.findBy(Relation.Type.EMERGES_IN,"domain",domainUri);

        if (rels.iterator().hasNext()){
            LOG.info("Deleting previous topics in domain: " + domainUri);

            ParallelExecutor pExecutor = new ParallelExecutor();
            for (Relation rel: rels){

                pExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        String topicUri = rel.getStartUri();

                        LOG.info("Deleting topic: " + topicUri);


                        clean(Relation.Type.MENTIONS_FROM_TOPIC, topicUri);
//                        columnRepository.findBy(Relation.Type.MENTIONS_FROM_TOPIC,"topic",topicUri)
//                                .forEach(mention -> udm.delete(Relation.Type.MENTIONS_FROM_TOPIC).byUri(mention.getUri()));

                        clean(Relation.Type.DEALS_WITH_FROM_DOCUMENT, topicUri);
//                        columnRepository.findBy(Relation.Type.DEALS_WITH_FROM_DOCUMENT,"topic",topicUri)
//                                .forEach(deals -> udm.delete(Relation.Type.DEALS_WITH_FROM_DOCUMENT).byUri(deals.getUri()));

                        clean(Relation.Type.DEALS_WITH_FROM_ITEM, topicUri);
//                        columnRepository.findBy(Relation.Type.DEALS_WITH_FROM_ITEM,"topic",topicUri)
//                                .forEach(deals -> udm.delete(Relation.Type.DEALS_WITH_FROM_ITEM).byUri(deals.getUri()));

                        clean(Relation.Type.DEALS_WITH_FROM_PART, topicUri);
//                        columnRepository.findBy(Relation.Type.DEALS_WITH_FROM_PART,"topic",topicUri)
//                                .forEach(deals -> udm.delete(Relation.Type.DEALS_WITH_FROM_PART).byUri(deals.getUri()));

                        LOG.info("Deleting EMERGES_IN from: " + topicUri);
                        udm.delete(Relation.Type.EMERGES_IN).byUri(rel.getUri());

//                        udm.delete(Resource.Type.TOPIC).byUri(topicUri);
                    }
                });
            }
            pExecutor.awaitTermination(30, TimeUnit.MINUTES);
            LOG.info("Topics (almost) deleted");


            Iterator<Relation> it = rels.iterator();
            while(it.hasNext()){
                String topicUri = it.next().getStartUri();
                udm.delete(Resource.Type.TOPIC).byUri(topicUri);
            }
            LOG.info("Topics deleted");
        }else{
            LOG.info("No topics in domain");
        }
    }

    private void clean(Relation.Type relType, String uri){
        LOG.info("Deleting " + relType.route() + " from: " + uri);
        Iterable<Relation> list = columnRepository.findBy(relType,"topic",uri);
        if (list.iterator().hasNext()) {
            columnRepository.delete(relType,list);
        }
    }

    public static void main(String[] args){

        System.out.println(factorial(BigInteger.valueOf(40)));
    }

    public static BigInteger factorial(BigInteger n) {
        if (n.compareTo(BigInteger.valueOf(1)) < 0)
            return BigInteger.valueOf(1);
        else
            return n.multiply(factorial(n.subtract(BigInteger.valueOf(1))));
    }

    @Test
    public void findDomainFromTopic() throws InterruptedException {
        try{


            String topicUri ="http://librairy.org/topics/3ddfe7c9d4cbde4e75700b75b44635d1";
            Iterable<Relation> rels = columnRepository.findBy(Relation.Type.EMERGES_IN, "topic", topicUri);

            for (Relation rel: rels){
                LOG.info("Relation: " + rel);
            }

            List<Resource> rels2 = udm.find(Resource.Type.DOMAIN).from(Resource.Type.TOPIC, topicUri);

            for (Resource rel: rels2){
                LOG.info("Resource: " + rel);
            }
        }catch (Exception e){
            LOG.error("error",e);
        }

    }

    @Test
    public void findDomainFromDocument() throws InterruptedException {
        try{

            String documentUri ="http://librairy.org/documents/f7c71cd15c54e367263de18bc657cdcb";
            Iterable<Relation> rels = columnRepository.findBy(Relation.Type.CONTAINS, "document", documentUri);

            for (Relation rel: rels){
                LOG.info("Relation: " + rel);
            }

        }catch (Exception e){
            LOG.error("error",e);
        }

    }



    @Test
    public void transitiveRelationships() throws InterruptedException {

        // Summary
        System.out.println("Number of Documents: "  + numDocs);
        System.out.println("Number of Items: "      + numItems);
        System.out.println("Number of Parts: "      + numParts);
        System.out.println("Number of Topics: "     + numTopics);
        System.out.println("Number of Words: "      + numWords);
        System.out.println("Number of Terms: "      + numTerms);
        System.out.println("====");
        System.out.println("Number of Provides: "   + numProvides);
        System.out.println("Number of Composes: "   + numComposes);
        System.out.println("Number of Contains: "   + numContains);
        System.out.println("Number of Bundles: "    + numBundles);
        System.out.println("Number of Describes: "  + numDescribes);
        System.out.println("Number of Deals_With from Docs: "   + numDealsWithDoc);
        System.out.println("Number of Deals_With from Item: "   + numDealsWithItem);
        System.out.println("Number of Deals_With from Parts: "   + numDealsWithPart);
        System.out.println("Number of EmergesIn: "      + numEmergesIn);
        System.out.println("Number of Mentions from Topics: "   + numMentionTopic);
        System.out.println("Number of Mentions from Terms: "    + numMentionTerm);
        System.out.println("Number of Similar Doc: "    + numSimilarDoc);
        System.out.println("Number of Similar Item: "   + numSimilarItem);
        System.out.println("Number of Similar Part: "   + numSimilarPart);
        System.out.println("Number of Embedded Words: " + numEmbedded);
        System.out.println("Number of AppearedIn: "     + numAppearedIn);
        System.out.println("Number of HypernymsOf: "     + numHypernyms);
        System.out.println("Number of PairsWith: "     + numPairs);

        Long start = System.currentTimeMillis();

        // Delete All
        udm.delete(org.librairy.model.domain.resources.Resource.Type.ANY).all();

        // Source
        org.librairy.model.domain.resources.Source source = org.librairy.model.domain.resources.Resource.newSource
                ("s1");
        udm.save(source);

        // Domain
        org.librairy.model.domain.resources.Domain domain = org.librairy.model.domain.resources.Resource.newDomain
                ("d1");
        udm.save(domain);
        udm.save(Relation.newComposes(source.getUri(),domain.getUri()));
        domainUri = domain.getUri();

        // Words
        List<org.librairy.model.domain.resources.Word> words = IntStream.range(0, NUM_WORDS).mapToObj(i -> org
                .librairy.model.domain.resources.Resource.newWord(String.valueOf(i))).collect(Collectors.toList());
        words.forEach(word -> {
            udm.save(word);
            udm.save(Relation.newEmbeddedIn(word.getUri(),domainUri));
        });


        // Terms
        List<org.librairy.model.domain.resources.Term> terms = IntStream.range(0, NUM_TERMS).mapToObj(i -> org
                .librairy.model.domain.resources.Resource.newTerm(String.valueOf(i))).collect(Collectors.toList());
        terms.forEach(term -> {
            udm.save(term);
            udm.save(Relation.newAppearedIn(term.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTerm(term.getUri(),word.getUri())));
        });

        // hypernym_of
        terms.forEach(t1 ->{
            terms.forEach(t2 ->{
                udm.save(Relation.newHypernymOf(t1.getUri(),t2.getUri(),domainUri));
            });
        });

        // pairs_with
        words.forEach(w1 ->{
            words.forEach(w2 ->{
                udm.save(Relation.newPairsWith(w1.getUri(),w2.getUri(), domainUri));
            });
        });

        // Topics
        List<org.librairy.model.domain.resources.Topic> docTopics = IntStream.range(0, NUM_DOC_TOPICS).mapToObj(i ->
                org.librairy.model.domain.resources.Resource.newTopic("d"+String.valueOf(i))).collect(Collectors.toList());
        docTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));

        });

        List<org.librairy.model.domain.resources.Topic> itemTopics = IntStream.range(0, NUM_ITEM_TOPICS).mapToObj(i
                -> org.librairy.model.domain.resources.Resource.newTopic("i"+String.valueOf(i))).collect(Collectors
                .toList());
        itemTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));

        });

        List<org.librairy.model.domain.resources.Topic> partTopics = IntStream.range(0, NUM_PART_TOPICS).mapToObj(i
                -> org.librairy.model.domain.resources.Resource.newTopic("p"+String.valueOf(i))).collect(Collectors
                .toList());
        partTopics.forEach(topic -> {
            udm.save(topic);
            udm.save(Relation.newEmergesIn(topic.getUri(),domainUri));
            words.forEach(word -> udm.save(Relation.newMentionsFromTopic(topic.getUri(),word.getUri())));
        });


        // Documents
        List<org.librairy.model.domain.resources.Part> parts            = new ArrayList<>();
        List<org.librairy.model.domain.resources.Item> items            = new ArrayList<>();
        List<org.librairy.model.domain.resources.Document> documents    = IntStream.range(0, NUM_DOCUMENTS).mapToObj
                (i -> org.librairy.model.domain.resources.Resource.newDocument(String.valueOf(i))).collect(Collectors
                .toList
                ());
        documents.forEach(doc -> {
            udm.save(doc);
            udm.save(Relation.newProvides(source.getUri(),doc.getUri()));
            udm.save(Relation.newContains(domainUri,doc.getUri()));
            docTopics.forEach(topic -> udm.save(Relation.newDealsWithFromDocument(doc.getUri(),topic.getUri())));

            // Items
            List<org.librairy.model.domain.resources.Item> internalItems    = IntStream.range(0, NUM_ITEMS).mapToObj
                    (i -> org.librairy.model.domain.resources.Resource.newItem(String.valueOf(i))).collect(Collectors
                    .toList());
            internalItems.forEach(item -> {
                udm.save(item);
                udm.save(Relation.newBundles(doc.getUri(),item.getUri()));
                itemTopics.forEach(topic -> udm.save(Relation.newDealsWithFromItem(item.getUri(),topic.getUri())));

                // Parts
                List<org.librairy.model.domain.resources.Part> internalParts    = IntStream.range(0, NUM_PARTS)
                        .mapToObj(i -> org.librairy.model.domain.resources.Resource.newPart(String.valueOf(i))).collect
                                (Collectors.toList());
                internalParts.forEach(part -> {
                    udm.save(part);
                    udm.save(Relation.newDescribes(part.getUri(),item.getUri()));
                    partTopics.forEach(topic -> udm.save(Relation.newDealsWithFromPart(part.getUri(),topic.getUri())));
                });
                parts.addAll(internalParts);

            });
            items.addAll(internalItems);
        });


        // -> similar_to document
        documents.forEach(d1 -> {
            documents.forEach(d2 -> {
                udm.save(Relation.newSimilarToDocuments(d1.getUri(),d2.getUri(), domainUri));
            });
        });


        // -> similar_to item
        items.forEach(i1 ->{
            items.forEach(i2 -> {
                udm.save(Relation.newSimilarToItems(i1.getUri(),i2.getUri(), domainUri));
            });
        });

        // -> similar_to item
        parts.forEach(p1 ->{
            parts.forEach(p2 -> {
                udm.save(Relation.newSimilarToParts(p1.getUri(),p2.getUri(), domainUri));
            });
        });

        boolean finished = false;
        long created = 0l;
        while(!finished){
            long time = 5000;
            Thread.sleep(time);
            LOG.info("Checking step");
            //int numDocs = udm.find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN, domainUri).size();
            //int res = udm.find(Relation.Type.SIMILAR_TO_PARTS).in(Resource.Type.DOMAIN, domainUri).size();
            long res = udm.count(Relation.Type.SIMILAR_TO_PARTS).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domainUri);
            double ratio = Double.valueOf(res-created)/Double.valueOf(time/1000);
            created = res;
            LOG.info("At this time: " + res + " similar parts [" + numSimilarPart + "] : " + ratio + "rows/sec");
            finished = res >= numSimilarPart;
        }


        Period period = new Interval(start, System.currentTimeMillis()).toPeriod();
        System.out.println("Time inserting data: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");

        evaluate();

        LOG.info("Deleting Topics...");
        start = System.currentTimeMillis();
        udm.find(Relation.Type.EMERGES_IN).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri).forEach(emerges -> udm.delete(org.librairy.model.domain.resources.Resource.Type.TOPIC).byUri(emerges.getStartUri()));
        period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.info("Deleted Topics in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");
        numTopics           = 0;
        numDealsWithDoc     = 0;
        numDealsWithItem    = 0;
        numDealsWithPart    = 0;
        numEmergesIn        = 0;
        numMentionTopic     = 0;
        evaluate();

        System.out.println("Delete PAIRS_WITH");
        udm.find(Relation.Type.PAIRS_WITH)
                .from(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domainUri)
                .parallelStream()
                .forEach(rel -> udm.delete(Relation.Type.PAIRS_WITH).byUri(rel.getUri()));
        numPairs            = 0;
        evaluate();

        System.out.println("Delete SIMILAR_TO DOCUMENT");
        udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS)
                .from(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domainUri)
                .parallelStream()
                .forEach(rel -> udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).byUri(rel.getUri()));
        numSimilarDoc       = 0;
        evaluate();


        System.out.println("Delete SIMILAR_TO ITEMS");
        udm.find(Relation.Type.SIMILAR_TO_ITEMS)
                .from(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domainUri)
                .parallelStream()
                .forEach(rel -> udm.delete(Relation.Type.SIMILAR_TO_ITEMS).byUri(rel.getUri()));
        numSimilarItem      = 0;
        evaluate();

        System.out.println("Delete SIMILAR_TO PARTS");
        udm.find(Relation.Type.SIMILAR_TO_PARTS)
                .from(org.librairy.model.domain.resources.Resource.Type.DOMAIN, domainUri)
                .parallelStream()
                .forEach(rel -> udm.delete(Relation.Type.SIMILAR_TO_PARTS).byUri(rel.getUri()));
        numSimilarPart      = 0;
        evaluate();

        System.out.println("Delete Terms");
        start = System.currentTimeMillis();
        udm.find(Relation.Type.APPEARED_IN).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri).forEach(appearedIn -> udm.delete(org.librairy.model.domain.resources.Resource.Type.TERM).byUri(appearedIn.getStartUri()));
        period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.info("Deleted Terms in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");
        numTerms            = 0;
        numHypernyms        = 0;
        numMentionTerm      = 0;
        numAppearedIn       = 0;
        evaluate();

        System.out.println("Delete Words");
        start = System.currentTimeMillis();
        udm.find(Relation.Type.EMBEDDED_IN).from(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri).forEach(embeddedIn -> udm.delete(org.librairy.model.domain.resources.Resource.Type.WORD).byUri(embeddedIn.getStartUri()));
        period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.info("Deleted Words in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds");
        numWords            = 0;
        numEmbedded         = 0;
        evaluate();

    }


    private void evaluate(){
        LOG.info("Evaluating Num Docs...");
        Assert.assertEquals(numDocs,            udm.count(Relation.Type.CONTAINS).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Words...");
        Assert.assertEquals(numWords,           udm.count(Relation.Type.EMBEDDED_IN).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Terms...");
        Assert.assertEquals(numTerms,           udm.count(Relation.Type.APPEARED_IN).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Items...");
        Assert.assertEquals(numItems,           udm.count(Relation.Type.BUNDLES).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Parts...");
        Assert.assertEquals(numParts,           udm.count(Relation.Type.DESCRIBES).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Topics...");
        Assert.assertEquals(numTopics,          udm.count(Relation.Type.EMERGES_IN).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Pairs...");
        Assert.assertEquals(numPairs,           udm.count(Relation.Type.PAIRS_WITH).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Hypernyms...");
        Assert.assertEquals(numHypernyms,       udm.count(Relation.Type.HYPERNYM_OF).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Mentions from Terms...");
        Assert.assertEquals(numMentionTerm,     udm.count(Relation.Type.MENTIONS_FROM_TERM).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Mentions from Topics...");
        Assert.assertEquals(numMentionTopic,    udm.count(Relation.Type.MENTIONS_FROM_TOPIC).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Deals With Doc...");
        Assert.assertEquals(numDealsWithDoc,    udm.count(Relation.Type.DEALS_WITH_FROM_DOCUMENT).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Deals With Item...");
        Assert.assertEquals(numDealsWithItem,   udm.count(Relation.Type.DEALS_WITH_FROM_ITEM).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Deals With Parts...");
        Assert.assertEquals(numDealsWithPart,   udm.count(Relation.Type.DEALS_WITH_FROM_PART).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Similar Docs...");
        Assert.assertEquals(numSimilarDoc,      udm.count(Relation.Type.SIMILAR_TO_DOCUMENTS).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Similar Items...");
        Assert.assertEquals(numSimilarItem,     udm.count(Relation.Type.SIMILAR_TO_ITEMS).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
        LOG.info("Evaluating Num Similar Parts...");
        Assert.assertEquals(numSimilarPart,     udm.count(Relation.Type.SIMILAR_TO_PARTS).in(org.librairy.model.domain.resources.Resource.Type.DOMAIN,domainUri));
    }

}
