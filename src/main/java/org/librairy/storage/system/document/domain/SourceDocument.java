package org.librairy.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Source;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="sources")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SourceDocument extends Source{

    @Override
    public org.librairy.model.domain.resources.Resource.Type getResourceType() {return org.librairy.model.domain.resources.Resource.Type.SOURCE;}

    @Id
    private String uri;

    private String domain;
}
