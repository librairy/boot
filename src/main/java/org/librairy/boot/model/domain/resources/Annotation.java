/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.cassandra.mapping.PrimaryKey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
@XmlRootElement(name = "annotation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Annotation extends Resource{

    @Override
    public Resource.Type getResourceType() {return Type.ANNOTATION;}

    public static final String RESOURCE="resource";
    private String resource;

    public static final String TYPE="type";
    private String type;

    public static final String CREATOR="creator";
    private String creator;

    public static final String FORMAT="format";
    private String format;

    public static final String LANGUAGE="language";
    private String language;

    public static final String VALUE="value";
    private Map<String,String> value;

    public static final String DESCRIPTION="description";
    private String description;

    public static final String PURPOSE="purpose";
    private String purpose;

    public static final String SCORE="score";
    private Double score;

    public static final String SELECTION="selection";
    private Map<String,String> selection;

}
