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
import org.librairy.boot.storage.system.graph.domain.nodes.TermNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="APPEARED_IN")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class AppearedInEdge extends Edge<TermNode,DomainNode> {

    @Property
    private Long times;

    @Property
    private long subtermOf;

    @Property
    private long supertermOf;

    @Property
    private double cvalue;

    @Property
    private double consensus;

    @Property
    private double pertinence;

    @Property
    private double probability;

    @Property
    private double termhood;




    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TERM;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOMAIN;
    }
}
