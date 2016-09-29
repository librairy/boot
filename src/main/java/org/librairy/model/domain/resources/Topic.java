/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.resources;

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
@XmlRootElement(name = "topic")
@XmlAccessorType(XmlAccessType.FIELD)
public class Topic extends Resource {

    @Override
    public Type getResourceType() {return Type.TOPIC;}

    public static final String CONTENT="content";
    private String content;

    public static final String ANALYSIS="analysis";
    private String analysis;
}
