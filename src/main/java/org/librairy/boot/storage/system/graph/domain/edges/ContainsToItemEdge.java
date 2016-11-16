/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.system.graph.domain.nodes.DomainNode;
import org.librairy.boot.storage.system.graph.domain.nodes.ItemNode;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="CONTAINS")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class ContainsToItemEdge extends Edge<DomainNode,ItemNode>{


    @Override
    public Resource.Type getStartType() {
        return Resource.Type.DOMAIN;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.ITEM;
    }
}
