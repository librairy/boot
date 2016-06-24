package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.PairsWithColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface PairsWithColumnRepository extends BaseColumnRepository<PairsWithColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from pairsWith where endUri = ?0")
    Iterable<PairsWithColumn> findByWord(String uri);

}