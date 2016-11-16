/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.PartColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface PartColumnRepository extends BaseColumnRepository<PartColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from parts where uri = ?0")
    Iterable<PartColumn> findByUri(String uri);

    @Query("select * from parts where creationTime = ?0")
    Iterable<PartColumn> findByCreationTime(String creationTime);
    
    @Query("select * from parts where sense = ?0")
    Iterable<PartColumn> findBySense(String sense);

    @Query("select * from parts where content = ?0")
    Iterable<PartColumn> findByContent(String content);

    @Query("select * from parts where tokens = ?0")
    Iterable<PartColumn> findByTokens(String tokens);

    @Query("select * from parts where startUri = ?0")
    Iterable<PartColumn> findByStart(String uri);

    @Query("select * from parts where endUri = ?0")
    Iterable<PartColumn> findByEnd(String uri);

}
