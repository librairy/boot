/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document.repository;

import org.librairy.boot.model.domain.resources.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface BaseDocumentRepository<T extends Resource> extends ElasticsearchRepository<T, String> {
}