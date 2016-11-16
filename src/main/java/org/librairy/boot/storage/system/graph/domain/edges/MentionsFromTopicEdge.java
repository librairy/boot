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
import org.librairy.boot.storage.system.graph.domain.nodes.TopicNode;
import org.librairy.boot.storage.system.graph.domain.nodes.WordNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of = {"uri"},callSuper = true)
public class MentionsFromTopicEdge extends Edge<TopicNode,WordNode> {

    @Property
    private Long times;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.TOPIC;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }
}
