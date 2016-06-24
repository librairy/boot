package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.DealsWithColumn;
import org.librairy.storage.system.column.domain.DescribeColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface DescribesColumnRepository extends BaseColumnRepository<DescribeColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from describes where startUri = ?0")
    Iterable<DescribeColumn> findByPart(String uri);

    @Query("select * from describes where endUri = ?0")
    Iterable<DescribeColumn> findByItem(String uri);

}
