package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "parts")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PartColumn extends org.librairy.model.domain.resources.Part {

}
