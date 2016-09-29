/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.system.graph.domain.nodes.TermNode;
import org.librairy.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="PROVIDES")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class ProvidesEdge extends Edge<TermNode,WordNode> {


    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TERM;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }
}
