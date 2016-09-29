/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Item;
import org.librairy.model.domain.resources.Resource;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="items")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemDocument extends Resource{

    @Override
    public Resource.Type getResourceType() {return Type.ITEM;}

    private String domain;

    public static final String AUTHORED_BY=Item.AUTHORED_BY;
    private String authoredBy;

    public static final String FORMAT=Item.FORMAT;
    private String format;

    public static final String LANGUAGE=Item.LANGUAGE;
    private String language;

    public static final String DESCRIPTION=Item.DESCRIPTION;
    private String description;

    public static final String URL=Item.URL;
    private String url;

    public static final String TYPE=Item.TYPE;
    private String type;

    public static final String CONTENT=Item.CONTENT;
    private String content;

}
