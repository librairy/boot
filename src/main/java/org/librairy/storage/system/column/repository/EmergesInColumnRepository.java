package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.EmergeInColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface EmergesInColumnRepository extends BaseColumnRepository<EmergeInColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from emergesIn where startUri = ?0")
    Iterable<EmergeInColumn> findByTopic(String uri);

    @Query("select * from emergesIn where endUri = ?0")
    Iterable<EmergeInColumn> findByDomain(String uri);

}
