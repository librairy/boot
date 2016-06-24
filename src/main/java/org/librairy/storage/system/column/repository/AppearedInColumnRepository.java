package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.AppearedInColumn;
import org.librairy.storage.system.column.domain.EmbeddedInColumn;
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

}
