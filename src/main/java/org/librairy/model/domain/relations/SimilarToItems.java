/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@XmlRootElement(name = "similarTo")
@XmlAccessorType(XmlAccessType.FIELD)
public class SimilarToItems extends SimilarTo {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getResourceType() {
        return org.librairy.model.domain.resources.Resource.Type.ITEM;
    }

    @Override
    public Relation.Type getType() {return Relation.Type.SIMILAR_TO_ITEMS;}
}
