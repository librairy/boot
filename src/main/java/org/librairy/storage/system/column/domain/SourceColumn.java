package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Source;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 21/12/15.
 */
@Table(value = "sources")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SourceColumn extends Source {

    @PrimaryKey
    private String uri;

}
