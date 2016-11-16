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
public class SimilarDocEdgeTemplate extends EdgeTemplate {

    public SimilarDocEdgeTemplate() {
        super(Relation.Type.SIMILAR_TO_DOCUMENTS);
    }

    @Override
    protected String label() {
        return "SIMILAR_TO";
    }

    @Override
    protected String pathBy(Resource.Type type) {
        switch (type){
            case ANY:           return "(s:Document)-[r:SIMILAR_TO]-(e:Document)";
            case DOMAIN:        return "(s:Document)-[r:SIMILAR_TO{domain:{0}}]-(e:Document)";
            case DOCUMENT:      return "(s:Document{uri:{0}})-[r:SIMILAR_TO]-(e:Document)";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected String pathBy(Relation.Type type) {
        switch (type){
            case SIMILAR_TO_DOCUMENTS:      return "(s:Document{uri:{0}})-[r:SIMILAR_TO]-(e:Document{uri:{1}})";
            default: throw new RuntimeException("Path for " + type.name() + " not implemented yet");
        }
    }

    @Override
    protected TemplateParameters paramsFrom(Relation relation) {
        return new TemplateParameters(relation).add("domain",relation.asSimilarToDocuments().getDomain());
    }
}
