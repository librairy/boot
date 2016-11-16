/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.EmbeddedInColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface EmbeddedInColumnRepository extends BaseColumnRepository<EmbeddedInColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from embeddedIn where startUri = ?0")
    Iterable<EmbeddedInColumn> findByWord(String uri);

    @Query("select * from embeddedIn where endUri = ?0")
    Iterable<EmbeddedInColumn> findByDomain(String uri);

    @Query("select * from embeddedIn where startUri = ?0")
    Iterable<EmbeddedInColumn> findByStart(String uri);

    @Query("select * from embeddedIn where endUri = ?0")
    Iterable<EmbeddedInColumn> findByEnd(String uri);

}
