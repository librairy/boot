package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.AggregateColumn;
import org.librairy.storage.system.column.domain.SimilarToColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface SimilarToColumnRepository extends BaseColumnRepository<SimilarToColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByDocument(String uri);

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByItem(String uri);

    @Query("select * from similarTo where endUri = ?0")
    Iterable<SimilarToColumn> findByPart(String uri);


}
