package org.librairy.storage.system.graph.edge;

import com.google.common.collect.Lists;
import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.Config;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Document;
import org.librairy.model.domain.resources.Domain;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.domain.resources.Term;
import org.librairy.storage.UDM;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "librairy.cassandra.contactpoints = drinventor.dia.fi.upm.es",
        "librairy.cassandra.port = 5011",
        "librairy.cassandra.keyspace = research",
        "librairy.elasticsearch.contactpoints = drinventor.dia.fi.upm.es",
        "librairy.elasticsearch.port = 5021",
        "librairy.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "librairy.neo4j.port = 5030",
        "librairy.eventbus.uri = amqp://librairy:drinventor@drinventor.dia.fi.upm.es:5041/drinventor"})
public class RelationTest {

    @Autowired
    UDM udm;

    @Test
    public void insertAppearedIn(){

        Domain domain = Resource.newDomain();
        udm.save(domain);

        List<String> res = udm.find(Resource.Type.TERM).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(res.isEmpty());

        Term term = Resource.newTerm();
        udm.save(term);

        org.librairy.model.domain.relations.AppearedIn appearedIn = Relation.newAppearedIn(term.getUri(), domain.getUri());
        udm.save(appearedIn);

        res = udm.find(Resource.Type.TERM).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(res.isEmpty());
        Assert.assertEquals(1,res.size());
        Assert.assertEquals(term.getUri(),res.get(0));

        Iterable<Relation> rel = udm.find(Relation.Type.APPEARED_IN).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(rel == null);

        List<Relation> rels = Lists.newArrayList(rel);
        Assert.assertEquals(1,rels.size());
        Assert.assertEquals(appearedIn,rels.get(0));

        udm.delete(Relation.Type.APPEARED_IN).byUri(appearedIn.getUri());

        res = udm.find(Resource.Type.TERM).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(res.isEmpty());

    }


    @Test
    public void insertSimilarTo(){

        Domain domain = Resource.newDomain();
        udm.save(domain);

        Document doc1 = Resource.newDocument();
        udm.save(doc1);
        udm.save(Relation.newContains(domain.getUri(),doc1.getUri()));

        Document doc2 = Resource.newDocument();
        udm.save(doc2);
        udm.save(Relation.newContains(domain.getUri(),doc2.getUri()));

        Document doc3 = Resource.newDocument();
        udm.save(doc3);
        udm.save(Relation.newContains(domain.getUri(),doc3.getUri()));


        List<Relation> rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(rels.isEmpty());

        udm.save(Relation.newSimilarToDocuments(doc1.getUri(),doc2.getUri()));
        rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertFalse(rels.isEmpty());
        Assert.assertEquals(1,rels.size());

        Assert.assertEquals(doc1.getUri(), rels.get(0).getStartUri());
        Assert.assertEquals(doc2.getUri(), rels.get(0).getEndUri());

        udm.delete(Relation.Type.SIMILAR_TO_DOCUMENTS).in(Resource.Type.DOMAIN,domain.getUri());
        rels = udm.find(Relation.Type.SIMILAR_TO_DOCUMENTS).from(Resource.Type.DOMAIN, domain.getUri());
        Assert.assertTrue(rels.isEmpty());
    }

}
