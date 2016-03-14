package org.librairy.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.system.graph.domain.nodes.DocumentNode;
import org.librairy.storage.system.graph.domain.nodes.DomainNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="CONTAINS")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class ContainsEdge extends Edge<DomainNode,DocumentNode>{


    @Override
    public Resource.Type getStartType() {
        return Resource.Type.DOMAIN;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOCUMENT;
    }
}
