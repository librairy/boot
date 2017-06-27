/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import com.google.common.base.Strings;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.boot.model.domain.resources.Domain;
import org.librairy.boot.model.domain.resources.Item;
import org.librairy.boot.model.domain.resources.Part;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.UDM;
import org.librairy.boot.storage.dao.DomainsDao;
import org.librairy.boot.storage.dao.PartsDao;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoTest.class)
public class DomainsDaoTest {

    private static final Logger LOG = LoggerFactory.getLogger(DomainsDaoTest.class);

    @Autowired
    DomainsDao dao;

    @Autowired
    UDM udm;

    String domainUri    = URIGenerator.fromId(Resource.Type.DOMAIN, "sampleDomain");
    String documentUri  = URIGenerator.fromId(Resource.Type.ITEM, "i1");
    String partUri      = URIGenerator.fromId(Resource.Type.PART, "p1");
    String subdomainUri = URIGenerator.fromId(Resource.Type.DOMAIN, "d1");

    @Before
    @After
    public void setup(){
        Arrays.stream(Resource.Type.values()).filter(type -> !type.equals(Resource.Type.ANY)).forEach( type -> {
            udm.delete(type).all();
            dao.delete(domainUri, type);
        });
        dao.delete(domainUri);
    }


    @Test
    public void crud(){

        Assert.assertFalse(dao.get(domainUri).isPresent());
        Assert.assertTrue(dao.list(100, Optional.empty(), false).isEmpty());
        Assert.assertTrue(dao.listDocuments(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertTrue(dao.listParts(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertTrue(dao.listSubdomains(domainUri, 100, Optional.empty(), false).isEmpty());

        Domain domain = new Domain();
        domain.setName("sampleDomain");
        domain.setUri(domainUri);
        dao.save(domain);

        Optional<Domain> domainResult = dao.get(domainUri);
        Assert.assertTrue(domainResult.isPresent());
        Assert.assertEquals(domain.getName(), domainResult.get().asDomain().getName());
        Assert.assertEquals(domain.getUri(), domainResult.get().asDomain().getUri());
        Assert.assertFalse(Strings.isNullOrEmpty(domainResult.get().asDomain().getCreationTime()));

        List<Domain> domainList = dao.list(100, Optional.empty(), false);
        Assert.assertFalse(domainList.isEmpty());
        Assert.assertEquals(1, domainList.size());

        Assert.assertTrue(dao.listDocuments(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertTrue(dao.listParts(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertTrue(dao.listSubdomains(domainUri, 100, Optional.empty(), false).isEmpty());

        // contains document
        Assert.assertFalse(dao.contains(domainUri, documentUri));

        // contains part
        Assert.assertFalse(dao.contains(domainUri, partUri));

        // contains subdomain
        Assert.assertFalse(dao.contains(domainUri, subdomainUri));

        // add document
        Assert.assertFalse(dao.addDocument(domainUri, documentUri));
        Item document = new Item();
        document.setDescription("sample-document");
        document.setUri(documentUri);
        udm.save(document);
        Assert.assertTrue(dao.addDocument(domainUri, documentUri));

        List<Item> docList = dao.listDocuments(domainUri, 100, Optional.empty(), false);
        Assert.assertFalse(docList.isEmpty());
        Assert.assertEquals(1, docList.size());

        // add part
        Assert.assertFalse(dao.addPart(domainUri, partUri));
        Part part = new Part();
        part.setSense("sample-part");
        part.setUri(partUri);
        udm.save(part);
        Assert.assertTrue(dao.addPart(domainUri, partUri));

        List<Part> partList = dao.listParts(domainUri, 100, Optional.empty(), false);
        Assert.assertFalse(partList.isEmpty());
        Assert.assertEquals(1, partList.size());

        // add subdomain
        Assert.assertFalse(dao.addSubdomain(domainUri, subdomainUri));
        Domain subdomain = new Domain();
        subdomain.setName("sample-subdomain");
        subdomain.setUri(subdomainUri);
        udm.save(subdomain);
        Assert.assertTrue(dao.addSubdomain(domainUri, subdomainUri));

        List<Domain> subdomainList = dao.listSubdomains(domainUri, 100, Optional.empty(), false);
        Assert.assertFalse(subdomainList.isEmpty());
        Assert.assertEquals(1, subdomainList.size());


        // contains document
        Assert.assertTrue(dao.contains(domainUri, documentUri));

        // contains part
        Assert.assertTrue(dao.contains(domainUri, partUri));

        // contains subdomain
        Assert.assertTrue(dao.contains(domainUri, subdomainUri));

        // remove document
        dao.removeDocument(domainUri, documentUri);
        Assert.assertTrue(dao.listDocuments(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertFalse(dao.contains(domainUri, documentUri));

        // remove part
        dao.removePart(domainUri, partUri);
        Assert.assertTrue(dao.listParts(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertFalse(dao.contains(domainUri, partUri));

        // remove subdomain
        dao.removeSubdomain(domainUri, subdomainUri);
        Assert.assertTrue(dao.listSubdomains(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertFalse(dao.contains(domainUri, subdomainUri));

        // clean subdomain from domains table
        udm.delete(Resource.Type.DOMAIN).byUri(subdomainUri);

        // add multiple documents
        IntStream.range(0,10).forEach( index -> {
            Item i = new Item();
            i.setDescription("sample-document");
            i.setUri(URIGenerator.fromId(Resource.Type.ITEM, String.valueOf(index)));
            udm.save(i);
            dao.addDocument(domainUri, URIGenerator.fromId(Resource.Type.ITEM, String.valueOf(index)));
        });
        Assert.assertFalse(dao.listDocuments(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertEquals(10, dao.listDocuments(domainUri, 100, Optional.empty(), false).size());

        // add multiple parts
        IntStream.range(0,10).forEach( index -> {
            Part p = new Part();
            p.setSense("sample-part");
            p.setUri(URIGenerator.fromId(Resource.Type.PART, String.valueOf(index)));
            udm.save(p);
            dao.addPart(domainUri, URIGenerator.fromId(Resource.Type.PART, String.valueOf(index)));
        });
        Assert.assertFalse(dao.listParts(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertEquals(10, dao.listParts(domainUri, 100, Optional.empty(), false).size());

        // add multiple subdomains
        IntStream.range(0,10).forEach( index -> {
            Domain s = new Domain();
            s.setName("sample-subdomain");
            s.setUri(URIGenerator.fromId(Resource.Type.DOMAIN, String.valueOf(index)));
            udm.save(s);
            dao.addSubdomain(domainUri, URIGenerator.fromId(Resource.Type.DOMAIN, String.valueOf(index)));
        });
        Assert.assertFalse(dao.listSubdomains(domainUri, 100, Optional.empty(), false).isEmpty());
        Assert.assertEquals(10, dao.listSubdomains(domainUri, 100, Optional.empty(), false).size());

        // remove all documents
        dao.removeAllDocuments(domainUri);
        Assert.assertTrue(dao.listDocuments(domainUri, 100, Optional.empty(), false).isEmpty());

        // remove all parts
        dao.removeAllParts(domainUri);
        Assert.assertTrue(dao.listParts(domainUri, 100, Optional.empty(), false).isEmpty());

        // remove all subdomains
        dao.removeAllSubdomains(domainUri);
        Assert.assertTrue(dao.listSubdomains(domainUri, 100, Optional.empty(), false).isEmpty());

        // Clean subdomains from domains table
        IntStream.range(0,10).forEach( index -> {
            udm.delete(Resource.Type.DOMAIN).byUri(URIGenerator.fromId(Resource.Type.DOMAIN, String.valueOf(index)));
        });

        dao.delete(domainUri);
        Assert.assertFalse(dao.get(domainUri).isPresent());
        Assert.assertTrue(dao.list(100, Optional.empty(), false).isEmpty());

    }

}
