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
@ToString(exclude = {"content","tokens"}, callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.ITEM;}

    public static final String AUTHORED_BY="authoredBY";
    private String authoredBy;

    public static final String FORMAT="format";
    private String format;

    public static final String LANGUAGE="language";
    private String language;

    public static final String DESCRIPTION="description";
    private String description;

    public static final String URL="url";
    private String url;

    public static final String TYPE="type";
    private String type;

    public static final String CONTENT="content";
    private String content;

    public static final String TOKENS="tokens";
    private String tokens;

}
