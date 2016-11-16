/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.EmergeInColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface EmergesInColumnRepository extends BaseColumnRepository<EmergeInColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from emergesIn where startUri = ?0")
    Iterable<EmergeInColumn> findByTopic(String uri);

    @Query("select * from emergesIn where endUri = ?0")
    Iterable<EmergeInColumn> findByDomain(String uri);

    @Query("select * from emergesIn where startUri = ?0")
    Iterable<EmergeInColumn> findByStart(String uri);

    @Query("select * from emergesIn where endUri = ?0")
    Iterable<EmergeInColumn> findByEnd(String uri);

}
