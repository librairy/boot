/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.boot.model.domain.resources.Topic;
import org.librairy.boot.model.domain.resources.Resource;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="topics")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TopicDocument extends Topic {

    @Override
    public Resource.Type getResourceType() {return Type.TOPIC;}

    private String domain;
}
