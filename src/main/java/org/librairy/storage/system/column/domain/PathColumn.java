package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Path;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "paths")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PathColumn extends Path {

    @PrimaryKey
    private String uri;

}
