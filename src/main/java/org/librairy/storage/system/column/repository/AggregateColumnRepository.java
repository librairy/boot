package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.AggregateColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface AggregateColumnRepository extends BaseColumnRepository<AggregateColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from aggregates where endUri = ?0")
    Iterable<AggregateColumn> findByDocument(String uri);

}
