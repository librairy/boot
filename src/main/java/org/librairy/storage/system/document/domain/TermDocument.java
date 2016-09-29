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
import org.librairy.model.domain.resources.Term;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="terms")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TermDocument extends Term {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getResourceType() {return org.librairy.model.domain.resources.Resource.Type.TERM;}

    private String domain;
}
