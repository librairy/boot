/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.ListenerColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface ListenerColumnRepository extends BaseColumnRepository<ListenerColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from listeners where uri = ?0")
    Iterable<ListenerColumn> findByUri(String uri);

    @Query("select * from listeners where creationTime = ?0")
    Iterable<ListenerColumn> findByCreationTime(String creationTime);

    @Query("select * from listeners where route = ?0")
    Iterable<ListenerColumn> findByRoute(String route);

    @Query("select * from listeners where startUri = ?0")
    Iterable<ListenerColumn> findByStart(String uri);

    @Query("select * from listeners where endUri = ?0")
    Iterable<ListenerColumn> findByEnd(String uri);

}
