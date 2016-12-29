/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage;

import lombok.Data;
import lombok.Getter;
import org.librairy.boot.model.modules.EventBus;
import org.librairy.boot.storage.dao.*;
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
@Data
public class Helper {

    @Autowired 
    UnifiedColumnRepository unifiedColumnRepository;

    @Autowired 
    UnifiedDocumentRepository unifiedDocumentRepository;

    @Autowired 
    UnifiedNodeGraphRepository unifiedNodeGraphRepository;

    @Autowired 
    UnifiedEdgeGraphRepository unifiedEdgeGraphRepository;

    @Autowired 
    UnifiedSession session;

    @Autowired 
    EventBus eventBus;

    @Autowired 
    TemplateFactory templateFactory;

    @Autowired 
    URIGenerator uriGenerator;

    @Autowired 
    QueryExecutor queryExecutor;

    @Autowired 
    KeyspaceDao keyspaceDao;

    @Autowired
    ParametersDao parametersDao;

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    PartsDao partsDao;

    @Autowired
    SubdomainsDao subdomainsDao;

    @Autowired 
    CounterDao counterDao;

}
