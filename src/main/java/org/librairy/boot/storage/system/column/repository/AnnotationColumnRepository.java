/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.librairy.boot.storage.system.column.domain.AnnotationColumn;
import org.librairy.boot.storage.system.column.domain.PartColumn;
import org.librairy.boot.storage.system.column.domain.WordColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface AnnotationColumnRepository extends BaseColumnRepository<AnnotationColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from annotations where uri = ?0")
    Iterable<AnnotationColumn> findByUri(String uri);

    @Query("select * from annotations where creationTime = ?0")
    Iterable<AnnotationColumn> findByCreationTime(String creationTime);

    @Query("select * from annotations where resource = ?0")
    Iterable<AnnotationColumn> findByResource(String resource);

    @Query("select * from annotations where type = ?0")
    Iterable<AnnotationColumn> findByType(String type);

    @Query("select * from annotations where creator = ?0")
    Iterable<AnnotationColumn> findByCreator(String creator);

    @Query("select * from annotations where purpose = ?0")
    Iterable<AnnotationColumn> findByPurpose(String purpose);

    @Query("select * from annotations where startUri = ?0")
    Iterable<AnnotationColumn> findByStart(String uri);

    @Query("select * from annotations where endUri = ?0")
    Iterable<AnnotationColumn> findByEnd(String uri);

}
