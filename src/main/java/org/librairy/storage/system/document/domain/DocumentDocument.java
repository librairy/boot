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
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="documents")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DocumentDocument extends org.librairy.model.domain.resources.Document {

    @Override
    public Type getResourceType() {return Type.DOCUMENT;}

    private String domain;
}
