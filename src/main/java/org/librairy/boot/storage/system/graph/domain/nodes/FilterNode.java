/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.boot.storage.system.graph.domain.edges.Edge;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Filter")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class FilterNode extends Node {

    private String content;

    @Override
    public void add(Edge edge) {

    }

    @Override
    public void remove(Edge edge) {

    }
}
