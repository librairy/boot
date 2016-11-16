/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
@XmlRootElement(name = "serializedobject")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializedObject extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.SERIALIZED_OBJECT;}

    public static final String INSTANCE="instance";
    protected Object instance;

    public <T> T getInstance(Class<T> clazz){
        if (instance == null) return null;
        return clazz.cast(instance);
    }

}
