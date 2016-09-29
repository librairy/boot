/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.document.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface BaseDocumentRepository<T extends org.librairy.model.domain.resources.Resource> extends ElasticsearchRepository<T, String> {
}
