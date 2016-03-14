package org.librairy.storage.system.graph.node;

import es.cbadenes.lab.test.IntegrationTest;
import org.librairy.model.domain.relations.Provides;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Document;
import org.librairy.model.domain.resources.Source;
import org.librairy.storage.system.graph.GraphConfig;
import org.librairy.storage.system.graph.repository.edges.UnifiedEdgeGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 03/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = {
        "librairy.neo4j.contactpoints = drinventor.dia.fi.upm.es",
        "librairy.neo4j.port = 5030" })
public class UnifiedGraphRelationalRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedGraphRelationalRepositoryTest.class);

    @Autowired
    UnifiedNodeGraphRepository unifiedNodeGraphRepository;

    @Autowired
    UnifiedEdgeGraphRepository unifiedEdgeGraphRepository;

    @Test
    public void sourceProvidesDocument(){

        unifiedNodeGraphRepository.deleteAll(org.librairy.model.domain.resources.Resource.Type.SOURCE);
        unifiedNodeGraphRepository.deleteAll(org.librairy.model.domain.resources.Resource.Type.DOCUMENT);

        Source source = org.librairy.model.domain.resources.Resource.newSource();
        source.setUri("sources/01");
        unifiedNodeGraphRepository.save(source);

        Document document = org.librairy.model.domain.resources.Resource.newDocument();
        document.setUri("documents/01");
        unifiedNodeGraphRepository.save(document);


        Provides provides = Relation.newProvides(source.getUri(), document.getUri());
        provides.setUri("provision/1");
        unifiedEdgeGraphRepository.save(provides);

        unifiedEdgeGraphRepository.delete(Relation.Type.PROVIDES,provides.getUri());

    }

    @Test
    public void findIn(){

        unifiedNodeGraphRepository.deleteAll(org.librairy.model.domain.resources.Resource.Type.SOURCE);
        unifiedNodeGraphRepository.deleteAll(org.librairy.model.domain.resources.Resource.Type.DOCUMENT);

        Source source = org.librairy.model.domain.resources.Resource.newSource();
        source.setUri("sources/01");
        unifiedNodeGraphRepository.save(source);

        Document document = org.librairy.model.domain.resources.Resource.newDocument();
        document.setUri("documents/01");
        unifiedNodeGraphRepository.save(document);

        Provides provides = Relation.newProvides(source.getUri(), document.getUri());
        provides.setUri("provision/1");
        unifiedEdgeGraphRepository.save(provides);

        Iterable<org.librairy.model.domain.resources.Resource> result = unifiedNodeGraphRepository.findFrom(org.librairy.model.domain.resources.Resource.Type.DOCUMENT, org.librairy.model.domain.resources.Resource.Type.SOURCE,source.getUri());
        System.out.println("Documents: " + result);

    }

}
