/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.SourceColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface SourceColumnRepository extends BaseColumnRepository<SourceColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from sources where uri = ?0")
    Iterable<SourceColumn> findByUri(String uri);

    @Query("select * from sources where creationTime = ?0")
    Iterable<SourceColumn> findByCreationTime(String creationTime);
    
    @Query("select * from sources where name = ?0")
    Iterable<SourceColumn> findByName(String name);

    @Query("select * from sources where description = ?0")
    Iterable<SourceColumn> findByDescription(String description);

    @Query("select * from sources where url = ?0")
    Iterable<SourceColumn> findByUrl(String url);

    @Query("select * from sources where protocol = ?0")
    Iterable<SourceColumn> findByProtocol(String protocol);

    @Query("select * from sources where startUri = ?0")
    Iterable<SourceColumn> findByStart(String uri);

    @Query("select * from sources where endUri = ?0")
    Iterable<SourceColumn> findByEnd(String uri);

}
