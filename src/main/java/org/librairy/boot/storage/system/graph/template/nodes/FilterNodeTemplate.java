/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.template.nodes;

import org.librairy.boot.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class FilterNodeTemplate extends NodeTemplate {

    public FilterNodeTemplate() {
        super(Resource.Type.FILTER);
    }

    @Override
    public String pathTo(Resource.Type type) {
        switch (type){
            default: throw new RuntimeException("Path to " + type + " not handled from " + this.type);
        }
    }
}
