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
@XmlRootElement(name = "analysis")
@XmlAccessorType(XmlAccessType.FIELD)
public class Analysis extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.ANALYSIS;}

    public static final String TYPE="type";
    private String type;

    public static final String DESCRIPTION="description";
    private String description;

    public static final String CONFIGURATION="configuration";
    private String configuration;

    public static final String DOMAIN="domain";
    private String domain;
}
