package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.relations.HypernymOf;
import org.librairy.model.domain.relations.PairsWith;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created on 15/04/16:
 *
 * @author cbadenes
 */
@Table(value = "hypernymOf")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HypernymOfColumn extends HypernymOf {

    @PrimaryKey
    private String uri;
}
