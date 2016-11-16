/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.AppearedInColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface AppearedInColumnRepository extends BaseColumnRepository<AppearedInColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from appearedIn where startUri = ?0")
    Iterable<AppearedInColumn> findByTerm(String uri);

    @Query("select * from appearedIn where endUri = ?0")
    Iterable<AppearedInColumn> findByDomain(String uri);

    @Query("select * from appearedIn where startUri = ?0")
    Iterable<AppearedInColumn> findByStart(String uri);

    @Query("select * from appearedIn where endUri = ?0")
    Iterable<AppearedInColumn> findByEnd(String uri);

}
