package org.librairy.storage.system.column.repository;

import org.librairy.storage.system.column.domain.DocumentColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface DocumentColumnRepository extends BaseColumnRepository<DocumentColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from documents where uri = ?0")
    Iterable<DocumentColumn> findByUri(String uri);

    @Query("select * from documents where creationTime = ?0")
    Iterable<DocumentColumn> findByCreationTime(String creationTime);
    
    @Query("select * from documents where publishedOn = ?0")
    Iterable<DocumentColumn> findByPublishedOn(String publishedOn);

    @Query("select * from documents where publishedBy = ?0")
    Iterable<DocumentColumn> findByPublishedBy(String publishedBy);

    @Query("select * from documents where authoredOn = ?0")
    Iterable<DocumentColumn> findByAuthoredOn(String authoredOn);

    @Query("select * from documents where authoredBy = ?0")
    Iterable<DocumentColumn> findByAuthoredBy(String authoredBy);

    @Query("select * from documents where contributedBy = ?0")
    Iterable<DocumentColumn> findByContributedBy(String contributedBy);

    @Query("select * from documents where retrievedFrom = ?0")
    Iterable<DocumentColumn> findByRetrievedFrom(String retrievedFrom);

    @Query("select * from documents where retrievedOn = ?0")
    Iterable<DocumentColumn> findByRetrievedOn(String retrievedOn);

    @Query("select * from documents where format = ?0")
    Iterable<DocumentColumn> findByFormat(String format);

    @Query("select * from documents where language = ?0")
    Iterable<DocumentColumn> findByLanguage(String language);

    @Query("select * from documents where title = ?0")
    Iterable<DocumentColumn> findByTitle(String title);

    @Query("select * from documents where subject = ?0")
    Iterable<DocumentColumn> findBySubject(String subject);

    @Query("select * from documents where description = ?0")
    Iterable<DocumentColumn> findByDescription(String description);

    @Query("select * from documents where rights = ?0")
    Iterable<DocumentColumn> findByRights(String rights);

    @Query("select * from documents where type = ?0")
    Iterable<DocumentColumn> findByType(String type);

    @Query("select * from documents where content = ?0")
    Iterable<DocumentColumn> findByContent(String content);

    @Query("select * from documents where tokens = ?0")
    Iterable<DocumentColumn> findByTokens(String tokens);
}
