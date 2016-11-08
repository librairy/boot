/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@XmlRootElement(name = "contains")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainsItem extends Contains {

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.ITEM;
    }

    @Override
    public Type getType() {return Type.CONTAINS_TO_ITEM;}
}
