/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.PathColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface PathColumnRepository extends BaseColumnRepository<PathColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from paths where uri = ?0")
    Iterable<PathColumn> findByUri(String uri);

    @Query("select * from paths where creationTime = ?0")
    Iterable<PathColumn> findByCreationTime(String creationTime);

    @Query("select * from paths where start = ?0")
    Iterable<PathColumn> findByStart(String start);

    @Query("select * from paths where end = ?0")
    Iterable<PathColumn> findByEnd(String end);

}
