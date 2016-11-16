/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.WordColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface WordColumnRepository extends BaseColumnRepository<WordColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from words where uri = ?0")
    Iterable<WordColumn> findByUri(String uri);

    @Query("select * from words where creationTime = ?0")
    Iterable<WordColumn> findByCreationTime(String creationTime);

    @Query("select * from words where content = ?0")
    Iterable<WordColumn> findByContent(String content);

    @Query("select * from words where lemma = ?0")
    Iterable<WordColumn> findByLemma(String lemma);

    @Query("select * from words where stem = ?0")
    Iterable<WordColumn> findByStem(String stem);

    @Query("select * from words where pos = ?0")
    Iterable<WordColumn> findByPos(String pos);

    @Query("select * from words where type = ?0")
    Iterable<WordColumn> findByType(String type);

    @Query("select * from words where startUri = ?0")
    Iterable<WordColumn> findByStart(String uri);

    @Query("select * from words where endUri = ?0")
    Iterable<WordColumn> findByEnd(String uri);
}
