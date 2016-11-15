/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.AggregateColumn;
import org.librairy.storage.system.column.domain.AnalysisColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface AnalysisColumnRepository extends BaseColumnRepository<AnalysisColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from analyses where uri = ?0")
    Iterable<AnalysisColumn> findByUri(String uri);

    @Query("select * from analyses where creationTime = ?0")
    Iterable<AnalysisColumn> findByCreationTime(String creationTime);
    
    @Query("select * from analyses where type = ?0")
    Iterable<AnalysisColumn> findByType(String type);

    @Query("select * from analyses where description = ?0")
    Iterable<AnalysisColumn> findByDescription(String description);

    @Query("select * from analyses where configuration = ?0")
    Iterable<AnalysisColumn> findByConfiguration(String configuration);

    @Query("select * from analyses where domain = ?0")
    Iterable<AnalysisColumn> findByDomain(String domain);

    @Query("select * from analyses where startUri = ?0")
    Iterable<AnalysisColumn> findByStart(String uri);

    @Query("select * from analyses where endUri = ?0")
    Iterable<AnalysisColumn> findByEnd(String uri);

}
