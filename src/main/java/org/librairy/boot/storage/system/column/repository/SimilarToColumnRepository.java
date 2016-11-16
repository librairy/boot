/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.SimilarToColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface SimilarToColumnRepository extends BaseColumnRepository<SimilarToColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByDocument(String uri);

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByItem(String uri);

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByPart(String uri);

    @Query("select * from similarTo where domain = ?0")
    Iterable<SimilarToColumn> findByDomain(String uri);

    @Query("select * from similarTo where startUri = ?0")
    Iterable<SimilarToColumn> findByStart(String uri);

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByEnd(String uri);

}
