/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.resources;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by cbadenes on 07/01/16.
 */
//TODO This class should be removed
@Data
public class MetaInformation implements Serializable{

    private String pubURI;
    private String pubFormat;
    private String sourceName;
    private String sourceUrl;
    private String sourceUri;
    private String title;
    private String published;
    private String authored;
    private String format;
    private String type;
    private String subject;
    private String language;
    private String rights;
    private String description;
    private String creators;
    private String contributors;

}
