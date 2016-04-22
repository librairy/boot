package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.ContainColumn;
import org.librairy.storage.system.column.domain.HypernymOfColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface HypernymColumnRepository extends BaseColumnRepository<HypernymOfColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from contains where endUri = ?0")
    Iterable<HypernymOfColumn> findByTerm(String uri);

}
