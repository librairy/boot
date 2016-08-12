package org.librairy.storage.system.document;

import org.librairy.storage.system.document.domain.ItemDocument;
import org.librairy.storage.system.document.repository.BaseDocumentRepository;
import org.librairy.storage.system.document.repository.ItemDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class ItemUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<ItemDocument> {

    @Autowired
    ItemDocumentRepository repository;

    @Override
    public BaseDocumentRepository<ItemDocument> getRepository() {
        return repository;
    }

    @Override
    public ItemDocument getEntity() {
        ItemDocument item = new ItemDocument();
        item.setUri("items/72ce5395-6268-439a-947e-802229e7f022");
        item.setCreationTime("2015-12-21T16:18:59Z");
        item.setFormat("pdf");
        item.setLanguage("en");
        item.setDescription("for testing purposes");
        item.setUrl("file:://opt/drinventor/example.pdf");
        item.setContent("Miniopterus aelleni is a bat in the genus Miniopterus found in the Comoro Islands and Madagascar. It is a small, brown bat, with a forearm length of 35 to 41 mm (1.4 to 1.6 in). The long tragus (a projection in the outer ear) has a broad base and a blunt or rounded tip. The uropatagium (tail membrane) is sparsely haired. The palate is flat and there are distinct diastemata (gaps) between the upper canines and premolars. Populations of this species were previously included in Miniopterus manavi, but recent molecular studies revealed that M");
        return item;
    }
}
