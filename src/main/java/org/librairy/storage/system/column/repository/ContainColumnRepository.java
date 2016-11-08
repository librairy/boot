/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.ContainColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface ContainColumnRepository extends BaseColumnRepository<ContainColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from contains where startUri = ?0")
    Iterable<ContainColumn> findByDomain(String uri);

    @Query("select * from contains where endUri = ?0")
    Iterable<ContainColumn> findByDocument(String uri);

    @Query("select * from contains where endUri = ?0")
    Iterable<ContainColumn> findByItem(String uri);

    @Query("select * from contains where endUri = ?0")
    Iterable<ContainColumn> findByPart(String uri);

}
