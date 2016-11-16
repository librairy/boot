/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.template.edges;

import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class MentionsTopicEdgeTemplate extends EdgeTemplate {

    public MentionsTopicEdgeTemplate() {
        super(Relation.Type.MENTIONS_FROM_TOPIC);
    }

    @Override
    protected String label() {
        return "MENTIONS";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Topic)-[r:MENTIONS]->(e:Word)";
            case DOMAIN:        return "(d:Domain{uri:{0}})<-[:EMERGES_IN]-(s:Topic)-[r:MENTIONS]->(e:Word)";
            case TOPIC:         return "(s:Topic{uri:{0}})-[r:MENTIONS]->(e:Word)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case MENTIONS_FROM_TOPIC:      return "(s:Topic{uri:{0}})-[r:MENTIONS]->(e:Word{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).
                add("times",relation.asMentionsFromTopic().getTimes())
                ;
    }
}
