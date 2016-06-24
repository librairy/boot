package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.relations.Aggregates;
import org.librairy.model.domain.relations.SimilarTo;
import org.librairy.model.domain.resources.Resource;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created on 15/04/16:
 *
 * @author cbadenes
 */
@Table(value = "similarTo")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SimilarToColumn extends SimilarTo {

    @PrimaryKey
    private String uri;

    @Override
    public Resource.Type getResourceType() {
        return null;
    }
}
