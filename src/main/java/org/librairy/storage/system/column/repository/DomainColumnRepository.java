/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.DomainColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface DomainColumnRepository extends BaseColumnRepository<DomainColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from domains where uri = ?0")
    Iterable<DomainColumn> findByUri(String uri);

    @Query("select * from domains where creationTime = ?0")
    Iterable<DomainColumn> findByCreationTime(String creationTime);
    
    @Query("select * from domains where name = ?0")
    Iterable<DomainColumn> findByName(String name);

    @Query("select * from domains where description = ?0")
    Iterable<DomainColumn> findByDescription(String description);

}
