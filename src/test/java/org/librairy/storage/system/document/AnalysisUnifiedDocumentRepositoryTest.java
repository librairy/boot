package org.librairy.storage.system.document;

import org.librairy.storage.system.document.domain.AnalysisDocument;
import org.librairy.storage.system.document.repository.AnalysisDocumentRepository;
import org.librairy.storage.system.document.repository.BaseDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class AnalysisUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<AnalysisDocument> {

    @Autowired
    AnalysisDocumentRepository repository;

    @Override
    public BaseDocumentRepository<AnalysisDocument> getRepository() {
        return repository;
    }

    @Override
    public AnalysisDocument getEntity() {
        AnalysisDocument document = new AnalysisDocument();
        document.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setType("topicModel");
        document.setConfiguration("alpha=16.1, beta=1.1, topics=8");
        document.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return document;
    }
}
