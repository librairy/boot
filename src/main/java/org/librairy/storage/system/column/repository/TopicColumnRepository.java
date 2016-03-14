package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.TopicColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface TopicColumnRepository extends BaseColumnRepository<TopicColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from topics where uri = ?0")
    Iterable<TopicColumn> findByUri(String uri);

    @Query("select * from topics where creationTime = ?0")
    Iterable<TopicColumn> findByCreationTime(String creationTime);
    
    @Query("select * from topics where analysis = ?0")
    Iterable<TopicColumn> findByAnalysis(String analysis);

    @Query("select * from topics where content = ?0")
    Iterable<TopicColumn> findByContent(String content);
}
