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
@XmlRootElement(name = "word")
@XmlAccessorType(XmlAccessType.FIELD)
public class Word extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.WORD;}

    public static final String CONTENT="content";
    private String content;

//    public static final String LEMMA="lemma";
//    private String lemma;
//
//    public static final String STEM="stem";
//    private String stem;
//
//    public static final String POS="pos";
//    private String pos;
//
//    public static final String TYPE="typeFilter";
//    private String typeFilter;
}
