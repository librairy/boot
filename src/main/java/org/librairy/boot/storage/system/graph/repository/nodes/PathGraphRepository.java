/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.nodes;

import org.librairy.boot.storage.system.graph.domain.nodes.PathNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PathGraphRepository extends ResourceGraphRepository<PathNode> {

    @Override
    PathNode findOneByUri(String uri);

}
