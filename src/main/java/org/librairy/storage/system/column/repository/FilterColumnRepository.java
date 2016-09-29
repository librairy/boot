/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.FilterColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface FilterColumnRepository extends BaseColumnRepository<FilterColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from filters where uri = ?0")
    Iterable<FilterColumn> findByUri(String uri);

    @Query("select * from filters where creationTime = ?0")
    Iterable<FilterColumn> findByCreationTime(String creationTime);

    @Query("select * from filters where content = ?0")
    Iterable<FilterColumn> findByContent(String content);

}
