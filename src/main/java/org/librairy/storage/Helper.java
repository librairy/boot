/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage;

import lombok.Getter;
import org.librairy.storage.executor.QueryExecutor;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.session.UnifiedSession;
import org.librairy.storage.system.column.repository.UnifiedColumnRepository;
import org.librairy.storage.system.document.repository.UnifiedDocumentRepository;
import org.librairy.storage.system.graph.repository.edges.UnifiedEdgeGraphRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.librairy.storage.system.graph.template.TemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 04/02/16.
 */
@Component
public class Helper {

    @Autowired @Getter
    UnifiedColumnRepository unifiedColumnRepository;

    @Autowired @Getter
    UnifiedDocumentRepository unifiedDocumentRepository;

    @Autowired @Getter
    UnifiedNodeGraphRepository unifiedNodeGraphRepository;

    @Autowired @Getter
    UnifiedEdgeGraphRepository unifiedEdgeGraphRepository;

    @Autowired @Getter
    UnifiedSession session;

    @Autowired @Getter
    org.librairy.model.modules.EventBus eventBus;

    @Autowired @Getter
    TemplateFactory templateFactory;

    @Autowired @Getter
    URIGenerator uriGenerator;

    @Autowired @Getter
    QueryExecutor queryExecutor;

}
