package org.librairy.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.storage.system.graph.domain.nodes.DocumentNode;
import org.librairy.storage.system.graph.domain.nodes.ItemNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="BUNDLES")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class BundlesEdge extends Edge<DocumentNode,ItemNode>{

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return org.librairy.model.domain.resources.Resource.Type.DOCUMENT;
    }

    @Override
    public org.librairy.model.domain.resources.Resource.Type getEndType() {
        return org.librairy.model.domain.resources.Resource.Type.ITEM;
    }
}
