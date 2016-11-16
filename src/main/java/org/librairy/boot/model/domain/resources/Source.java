/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.resources;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
@XmlRootElement(name = "source")
@XmlAccessorType(XmlAccessType.FIELD)
public class Source extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.SOURCE;}

    public static final String NAME="name";
    private String name = "";

    public static final String DESCRIPTION="description";
    private String description = "";

    public static final String URL="url";
    private String url = "";

    public static final String PROTOCOL="protocol";
    private String protocol = "";

    public String getProtocol(){
        if (Strings.isNullOrEmpty(protocol)){
            return StringUtils.substringBefore(url,":");
        }
        return protocol;
    }

    public String getName(){
        if (Strings.isNullOrEmpty(name)){
            return StringUtils.substringBetween(url+"/","//","/");
        }
        return name;
    }

    public String extractServer(){
        return StringUtils.substringBefore(url, "?");
    }
}
