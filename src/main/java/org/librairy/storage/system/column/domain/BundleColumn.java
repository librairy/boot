package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.relations.Bundles;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created on 15/04/16:
 *
 * @author cbadenes
 */
@Table(value = "bundles")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BundleColumn extends Bundles {

}
