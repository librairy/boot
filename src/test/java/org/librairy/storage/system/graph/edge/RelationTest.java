package org.librairy.storage.system.graph.edge;

import com.google.common.collect.Lists;
import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.Config;
import org.librairy.model.domain.relations.Contains;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.relations.SimilarToDocuments;
import org.librairy.model.domain.resources.Document;
import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Term;
import org.librairy.model.utils.TimeUtils;
import org.librairy.storage.UDM;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 22/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.cassandra.contactpoints = 192.168.99.100",
        "librairy.cassandra.port = 5011",
        "librairy.cassandra.keyspace = research",
        "librairy.elasticsearch.contactpoints = 192.168.99.100",
        "librairy.elasticsearch.port = 5021",
        "librairy.neo4j.contactpoints = 192.168.99.100",
        "librairy.neo4j.port = 5030",
        "librairy.eventbus.host = localhost",
        "librairy.eventbus.port = 5041",
})
public class RelationTest {

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Test
    public void insertAppearedIn(){

        Domain domain = Resource.newDomain("d");
        udm.save(domain);

        List<Resource> resources = udm.find(Resource.Type.TERM).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(resources.isEmpty());

        Term term = Resource.newTerm("t");
        udm.save(term);

        org.librairy.model.domain.relations.AppearedIn appearedIn = Relation.newAppearedIn(term.getUri(), domain.getUri());
        udm.save(appearedIn);

        resources = udm.find(Resource.Type.TERM).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(resources.isEmpty());
        Assert.assertEquals(1,resources.size());
        Assert.assertEquals(term.getUri(),resources.get(0));

        Iterable<Relation> rel = udm.find(Relation.Type.APPEARED_IN).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(rel == null);

        List<Relation> rels = Lists.newArrayList(rel);
        Assert.assertEquals(1,rels.size());
        Assert.assertEquals(appearedIn,rels.get(0));

        udm.delete(Relation.Type.APPEARED_IN).byUri(appearedIn.getUri());

        resources = udm.find(Resource.Type.TERM).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(resources.isEmpty());

    }


    @Test
    public void insertSimilarTo(){

        Domain domain = Resource.newDomain("d");
        udm.save(domain);

        Document doc1 = Resource.newDocument("d1");
        udm.save(doc1);
        udm.save(Relation.newContains(domain.getUri(),doc1.getUri()));

        Document doc2 = Resource.newDocument("d2");
        udm.save(doc2);
        udm.save(Relation.newContains(domain.getUri(),doc2.getUri()));

        Document doc3 = Resource.newDocument("d3");
        udm.save(doc3);
        udm.save(Relation.newContains(domain.getUri(),doc3.getUri()));


        List<Relation> rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(rels.isEmpty());

        udm.save(Relation.newSimilarToDocuments(doc1.getUri(),doc2.getUri(), domain.getUri()));
        rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(1,rels.size());

        Assert.assertEquals(doc1.getUri(), rels.get(0).getStartUri());
        Assert.assertEquals(doc2.getUri(), rels.get(0).getEndUri());

        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,domain.getUri());
        rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(rels.isEmpty());
    }

    @Test
    public void checkDuplicates(){

        udm.find(Resource.Type.DOMAIN).all().stream().forEach(resource -> udm.delete(Resource.Type.DOMAIN).byUri
                (resource.getUri()));
        udm.find(Resource.Type.DOCUMENT).all().stream().forEach(resource -> udm.delete(Resource.Type.DOCUMENT).byUri
                (resource.getUri()));
        udm.find(Relation.Type.CONTAINS).all().stream().forEach(rel -> udm.delete(Relation.Type.CONTAINS).byUri(rel.getUri()));

        Domain domain = Resource.newDomain("d");
        udm.save(domain);

        List<Resource> resources = udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(resources.isEmpty());

        Document doc = Resource.newDocument("d1");
        udm.save(doc);

        String relUri = uriGenerator.from(Relation.Type.CONTAINS,"sample");
        String creationTime = TimeUtils.asISO();
        Contains contains = Relation.newContains(domain.getUri(), doc.getUri());
        contains.setUri(relUri);
        contains.setCreationTime(creationTime);
        udm.save(contains);

        List<Relation> rels = udm.find(Relation.Type.CONTAINS).btw(domain.getUri(), doc.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(1,rels.size());
        Assert.assertEquals(relUri,rels.get(0).getUri());

        // Same uri and creationTime
        Contains contains2 = Relation.newContains(domain.getUri(), doc.getUri());
        contains2.setUri(relUri);
        contains2.setCreationTime(creationTime);
        udm.save(contains2);

        rels = udm.find(Relation.Type.CONTAINS).btw(domain.getUri(), doc.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(1,rels.size());
        Assert.assertEquals(relUri,rels.get(0).getUri());


        // Same uri and different creationTime
        Contains contains3 = Relation.newContains(domain.getUri(), doc.getUri());
        contains3.setUri(relUri);
        contains3.setCreationTime(TimeUtils.asISO());
        udm.save(contains3);

        rels = udm.find(Relation.Type.CONTAINS).btw(domain.getUri(), doc.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(1,rels.size());
        Assert.assertEquals(relUri,rels.get(0).getUri());
        Assert.assertEquals(contains3.getCreationTime(),rels.get(0).getCreationTime());


        // Different uri and creationTime
        String relUri2 = uriGenerator.from(Relation.Type.CONTAINS,"sample2");
        String creationTime2 = TimeUtils.asISO();
        Contains contains4 = Relation.newContains(domain.getUri(), doc.getUri());
        contains4.setUri(relUri2);
        contains4.setCreationTime(creationTime2);
        udm.save(contains4);

        rels = udm.find(Relation.Type.CONTAINS).btw(domain.getUri(), doc.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(2,rels.size());
        Assert.assertTrue(rels.stream().map(rel -> rel.getUri()).collect(Collectors.toList()).contains(relUri));
        Assert.assertTrue(rels.stream().map(rel -> rel.getUri()).collect(Collectors.toList()).contains(relUri2));


    }

    @Test
    public void checkDuplicatesInSimilarRelations(){

        udm.find(Resource.Type.DOMAIN).all().stream().forEach(resource -> udm.delete(Resource.Type.DOMAIN).byUri
                (resource.getUri()));
        udm.find(Resource.Type.DOCUMENT).all().stream().forEach(resource -> udm.delete(Resource.Type.DOCUMENT).byUri
                (resource.getUri()));
        udm.find(Relation.Type.CONTAINS).all().stream().forEach(rel -> udm.delete(Relation.Type.CONTAINS).byUri(rel.getUri()));
        udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).all().stream().forEach(rel -> udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).byUri
                (rel.getUri
                ()));

        Domain domain1 = Resource.newDomain("d1");
        udm.save(domain1);

        Domain domain2 = Resource.newDomain("d2");
        udm.save(domain2);

        List<Resource> resources = udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOMAIN, domain1.getUri());
        Assert.assertTrue(resources.isEmpty());

        Document doc1 = Resource.newDocument("doc1");
        udm.save(doc1);
        udm.save(Relation.newContains(domain1.getUri(), doc1.getUri()));
        udm.save(Relation.newContains(domain2.getUri(), doc1.getUri()));


        Document doc2 = Resource.newDocument("doc2");
        udm.save(doc2);
        udm.save(Relation.newContains(domain1.getUri(), doc2.getUri()));
        udm.save(Relation.newContains(domain2.getUri(), doc2.getUri()));


        SimilarToDocuments s1 = Relation.newSimilarToDocuments(doc1.getUri(),
                doc2.getUri(), domain1.getUri());
        udm.save(s1);

        List<Relation> res1 = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain1.getUri());
        Assert.assertFalse(res1.isEmpty());
        Assert.assertEquals(1,res1.size());
        Assert.assertEquals(s1.getUri(), res1.get(0).getUri());


        SimilarToDocuments s2 = Relation.newSimilarToDocuments(doc1.getUri(),
                doc2.getUri(), domain2.getUri());
        udm.save(s2);

        List<Relation> res2 = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain2.getUri());
        Assert.assertFalse(res2.isEmpty());
        Assert.assertEquals(1,res2.size());
        Assert.assertEquals(s2.getUri(), res2.get(0).getUri());


    }

    @Test
    public void readRelations(){

        udm.find(Resource.Type.DOMAIN).all().stream().forEach(resource -> udm.delete(Resource.Type.DOMAIN).byUri
                (resource.getUri()));
        udm.find(Resource.Type.DOCUMENT).all().stream().forEach(resource -> udm.delete(Resource.Type.DOCUMENT).byUri
                (resource.getUri()));
        udm.find(Relation.Type.CONTAINS).all().stream().forEach(rel -> udm.delete(Relation.Type.CONTAINS).byUri(rel.getUri()));
        udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).all().stream().forEach(rel -> udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).byUri
                (rel.getUri
                        ()));

        Domain domain1 = Resource.newDomain("d1");
        udm.save(domain1);

        Domain domain2 = Resource.newDomain("d2");
        udm.save(domain2);

        Document doc1 = Resource.newDocument("doc1");
        udm.save(doc1);
        udm.save(Relation.newContains(domain1.getUri(), doc1.getUri()));
        udm.save(Relation.newContains(domain2.getUri(), doc1.getUri()));

        List<Resource> resources = udm.find(Resource.Type.DOCUMENT).from(Resource.Type.DOMAIN, domain1.getUri());
        Assert.assertFalse(resources.isEmpty());

        Resource res = resources.get(0);

        System.out.println(res);


    }

}
