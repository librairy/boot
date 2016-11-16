/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage;

import lombok.Getter;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.storage.executor.QueryExecutor;
import org.librairy.boot.storage.generator.URIGenerator;
import org.librairy.boot.storage.system.column.repository.UnifiedColumnRepository;
import org.librairy.boot.storage.system.document.repository.UnifiedDocumentRepository;
import org.librairy.boot.storage.system.graph.repository.edges.UnifiedEdgeGraphRepository;
import org.librairy.boot.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.librairy.boot.storage.system.graph.template.TemplateFactory;
import org.librairy.boot.storage.session.UnifiedSession;
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
    EventBus eventBus;

    @Autowired @Getter
    TemplateFactory templateFactory;

    @Autowired @Getter
    URIGenerator uriGenerator;

    @Autowired @Getter
    QueryExecutor queryExecutor;

}
