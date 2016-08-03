package org.librairy.storage.system.graph.repository.nodes;

import org.librairy.storage.system.graph.domain.nodes.FilterNode;
import org.librairy.storage.system.graph.domain.nodes.WordNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface FilterGraphRepository extends ResourceGraphRepository<FilterNode> {

    @Override
    FilterNode findOneByUri(String uri);

}
