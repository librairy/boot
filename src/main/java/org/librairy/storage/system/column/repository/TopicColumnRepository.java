/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.TermColumn;
import org.librairy.storage.system.column.domain.TopicColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface TopicColumnRepository extends BaseColumnRepository<TopicColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from topics where uri = ?0")
    Iterable<TopicColumn> findByUri(String uri);

    @Query("select * from topics where creationTime = ?0")
    Iterable<TopicColumn> findByCreationTime(String creationTime);
    
    @Query("select * from topics where analysis = ?0")
    Iterable<TopicColumn> findByAnalysis(String analysis);

    @Query("select * from topics where content = ?0")
    Iterable<TopicColumn> findByContent(String content);

    @Query("select * from topics where startUri = ?0")
    Iterable<TopicColumn> findByStart(String uri);

    @Query("select * from topics where endUri = ?0")
    Iterable<TopicColumn> findByEnd(String uri);
}
