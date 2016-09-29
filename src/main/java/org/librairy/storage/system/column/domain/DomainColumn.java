/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Domain;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 21/12/15.
 */
@Table(value = "domains")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainColumn extends Domain {

}
