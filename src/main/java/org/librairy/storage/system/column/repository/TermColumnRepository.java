/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.SourceColumn;
import org.librairy.storage.system.column.domain.TermColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface TermColumnRepository extends BaseColumnRepository<TermColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from terms where uri = ?0")
    Iterable<TermColumn> findByUri(String uri);

    @Query("select * from terms where creationTime = ?0")
    Iterable<TermColumn> findByCreationTime(String creationTime);

    @Query("select * from terms where content = ?0")
    Iterable<TermColumn> findByContent(String content);

    @Query("select * from terms where startUri = ?0")
    Iterable<TermColumn> findByStart(String uri);

    @Query("select * from terms where endUri = ?0")
    Iterable<TermColumn> findByEnd(String uri);

}
