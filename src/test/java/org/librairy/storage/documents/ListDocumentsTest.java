package org.librairy.storage.documents;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.cbadenes.lab.test.IntegrationTest;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.Config;
import org.librairy.model.domain.resources.Document;
import org.librairy.storage.Helper;
import org.librairy.storage.UDM;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.document.repository.WordDocumentRepository;
import org.librairy.storage.system.graph.repository.edges.DealsWithFromDocumentEdgeRepository;
import org.librairy.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.SourceGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 01/01/16.
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
        "librairy.eventbus.host = drinventor.dia.fi.upm.es"})
public class ListDocumentsTest {

    private static final Logger LOG = LoggerFactory.getLogger(ListDocumentsTest.class);

    @Autowired
    UDM udm;

    @Autowired
    Helper helper;

    @Autowired
    Session session;

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
    org.librairy.model.modules.EventBus eventBus;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    UnifiedNodeGraphRepositoryFactory factory;


    @Test
    public void listOfDocuments() throws IOException, URISyntaxException {

        List<String> refFiles = Arrays.asList(new String[]{"a109-rivers.pdf",
                "a120-gutierrez",
                "a13-irving",
                "a142-alexa",
                "a148-daniels",
                "a27-lee",

                "a37-talvala",
                "a38-zheng",
                "a53-barbic",
                "a54-sun",
                "a55-palacios",
                "a57-chuang",
                "a57-mohammed",
                "a61-hasan",
                "a62-mahajan",
                "a63-wang",
                "a65-rosenberg",
                "a71-tagliasacchi",
                "a74-tang",
                "a76-wojtan",
                "a85-lau",
                "a87-ballan",
                "a87-harmon",
                "a88-neubert",
                "a95-fattal",
                "a98-dong",
                "a99-kopf",
                "p1169-wang",
                "p294-agarwala",
                "p295-smith",
                "p380-alexa",
                "p393-james",
                "p501-govindaraju",
                "p519-weiss",
                "p594-bridson",
                "p600-zitnick",
                "p617-alliez",
                "p631-jia",
                "p635-peng",
                "p681-liu",
                "p697-igarashi",
                "p698-tsang",
                "p716-treuille",
                "p777-lefebvre",
                "p805-irving",
                "p814-matusik",
                "p828-agrawal",
                "p862-goldman",
                "p869-borgeat",
                "p932-macintyre"});


        List<String> newFiles = Arrays.asList(new String[]{"a111-li.pdf.ser",
                "a21-adams.pdf.ser",
                "a50-kaufmann.pdf.ser",
                "a68-green.pdf.ser",
                "a95-agrawal.pdf.ser",
                "p1089-hasan.pdf.ser",
                "p257-durand.pdf.ser",
                "p368-liu.pdf.ser",
                "p735-lawrence.pdf.ser"});


//        udm.save(new Item());
//
        List<String> uris = udm.find(org.librairy.model.domain.resources.Resource.Type.DOCUMENT).all();
//
        LOG.info("Item URIs: " + uris);

        List<Document> documents = uris.stream().map(uri -> udm.read(org.librairy.model.domain.resources.Resource.Type.DOCUMENT).byUri(uri).get().asDocument()).filter(doc -> newFiles.contains(doc.getRetrievedFrom())).collect(Collectors.toList());

        List<Reference> references = new ArrayList<>();
        for (Document document : documents){
            LOG.info("File: " + document);
            try{
                Reference reference = new Reference();
                reference.setFileName(StringUtils.substringBefore(document.getRetrievedFrom(),".pdf") + ".pdf");
                reference.setTitle(document.getTitle());
                reference.setUri(document.getUri());
                references.add(reference);
            }catch (Exception e){
                LOG.error("Error on file: " + document.getUri(),e);
            }

        }


        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File("new-documents.json"), references);


    }

}
