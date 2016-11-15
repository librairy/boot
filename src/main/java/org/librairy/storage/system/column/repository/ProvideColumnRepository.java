/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.PartColumn;
import org.librairy.storage.system.column.domain.ProvideColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface ProvideColumnRepository extends BaseColumnRepository<ProvideColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from provides where endUri = ?0")
    Iterable<ProvideColumn> findByDocument(String uri);

    @Query("select * from provides where startUri = ?0")
    Iterable<ProvideColumn> findBySource(String uri);

    @Query("select * from provides where startUri = ?0")
    Iterable<ProvideColumn> findByStart(String uri);

    @Query("select * from provides where endUri = ?0")
    Iterable<ProvideColumn> findByEnd(String uri);

}
