package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.relations.Aggregates;
import org.librairy.model.domain.relations.Contains;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created on 15/04/16:
 *
 * @author cbadenes
 */
@Table(value = "aggregates")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AggregateColumn extends Aggregates {

    @PrimaryKey
    private String uri;

}
