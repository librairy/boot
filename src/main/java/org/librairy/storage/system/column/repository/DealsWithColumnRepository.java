/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.ContainColumn;
import org.librairy.storage.system.column.domain.DealsWithColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface DealsWithColumnRepository extends BaseColumnRepository<DealsWithColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from dealsWith where startUri = ?0")
    Iterable<DealsWithColumn> findByDocument(String uri);

    @Query("select * from dealsWith where startUri = ?0")
    Iterable<DealsWithColumn> findByItem(String uri);

    @Query("select * from dealsWith where startUri = ?0")
    Iterable<DealsWithColumn> findByPart(String uri);

    @Query("select * from dealsWith where endUri = ?0")
    Iterable<DealsWithColumn> findByTopic(String uri);

    @Query("select * from dealsWith where startUri = ?0")
    Iterable<DealsWithColumn> findByStart(String uri);

    @Query("select * from dealsWith where endUri = ?0")
    Iterable<DealsWithColumn> findByEnd(String uri);

}
