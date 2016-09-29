/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.nodes;

import org.librairy.storage.system.graph.domain.nodes.FilterNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface FilterGraphRepository extends ResourceGraphRepository<FilterNode> {

    @Override
    FilterNode findOneByUri(String uri);

}
