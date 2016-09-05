package org.librairy.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="topics")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TopicDocument extends org.librairy.model.domain.resources.Topic {

    @Override
    public Resource.Type getResourceType() {return Type.TOPIC;}

    private String domain;
}
