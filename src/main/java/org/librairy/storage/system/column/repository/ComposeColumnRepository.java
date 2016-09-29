/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.ComposeColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface ComposeColumnRepository extends BaseColumnRepository<ComposeColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from composes where startUri = ?0")
    Iterable<ComposeColumn> findBySource(String uri);

    @Query("select * from composes where endUri = ?0")
    Iterable<ComposeColumn> findByDomain(String uri);


}
