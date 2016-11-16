/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.ItemColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface ItemColumnRepository extends BaseColumnRepository<ItemColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from items where uri = ?0")
    Iterable<ItemColumn> findByUri(String uri);

    @Query("select * from items where creationTime = ?0")
    Iterable<ItemColumn> findByCreationTime(String creationTime);
    
    @Query("select * from items where authoredOn = ?0")
    Iterable<ItemColumn> findByAuthoredOn(String authoredOn);

    @Query("select * from items where authoredBy = ?0")
    Iterable<ItemColumn> findByAuthoredBy(String authoredBy);

    @Query("select * from items where contributedBy = ?0")
    Iterable<ItemColumn> findByContributedBy(String contributedBy);
    
    @Query("select * from items where format = ?0")
    Iterable<ItemColumn> findByFormat(String format);

    @Query("select * from items where language = ?0")
    Iterable<ItemColumn> findByLanguage(String language);

    @Query("select * from items where title = ?0")
    Iterable<ItemColumn> findByTitle(String title);

    @Query("select * from items where subject = ?0")
    Iterable<ItemColumn> findBySubject(String subject);

    @Query("select * from items where description = ?0")
    Iterable<ItemColumn> findByDescription(String description);

    @Query("select * from items where url = ?0")
    Iterable<ItemColumn> findByUrl(String url);

    @Query("select * from items where type = ?0")
    Iterable<ItemColumn> findByType(String type);

    @Query("select * from items where content = ?0")
    Iterable<ItemColumn> findByContent(String content);

    @Query("select * from items where tokens = ?0")
    Iterable<ItemColumn> findByTokens(String tokens);

    @Query("select * from items where startUri = ?0")
    Iterable<ItemColumn> findByStart(String uri);

    @Query("select * from items where endUri = ?0")
    Iterable<ItemColumn> findByEnd(String uri);
}
