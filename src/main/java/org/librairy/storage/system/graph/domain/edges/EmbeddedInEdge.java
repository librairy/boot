package org.librairy.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.storage.system.graph.domain.nodes.ItemNode;
import org.librairy.storage.system.graph.domain.nodes.PartNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="EMBEDDED_IN")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class EmbeddedInEdge extends Edge<PartNode,ItemNode> {

    @Property
    private float[] vector;

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return org.librairy.model.domain.resources.Resource.Type.PART;
    }

    @Override
    public org.librairy.model.domain.resources.Resource.Type getEndType() {
        return org.librairy.model.domain.resources.Resource.Type.ITEM;
    }
}
