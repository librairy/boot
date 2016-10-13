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
@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
public class File extends Resource {

    @Override
    //TODO to be included in Resource.Type
    public Resource.Type getResourceType() {return Type.ANY;}

    public static final String URL="url";
    private String url = "";

    public static final String SOURCE="source";
    private String source = "";

    // TODO this field should be deleted
    public static final String DOMAIN="domain";
    private String domain = "";

    public static final String AGGREGATEDFROM="aggregatedFrom";
    private String aggregatedFrom;

    public static final String METAINFORMATION="metaInformation";
    private MetaInformation metaInformation;

}
