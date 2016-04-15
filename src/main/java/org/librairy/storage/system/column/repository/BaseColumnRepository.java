package org.librairy.storage.system.column.repository;


import org.librairy.model.domain.LinkableElement;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 21/12/15.
 */
@NoRepositoryBean
public interface BaseColumnRepository<T extends LinkableElement> extends CassandraRepository<T> {

}
