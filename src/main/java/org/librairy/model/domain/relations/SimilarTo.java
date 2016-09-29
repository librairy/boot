/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class SimilarTo extends Relation{

    @Getter @Setter
    private String domain;

    public abstract org.librairy.model.domain.resources.Resource.Type getResourceType();

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return getResourceType();
    }

    @Override
    public org.librairy.model.domain.resources.Resource.Type getEndType() {
        return getResourceType();
    }

}
