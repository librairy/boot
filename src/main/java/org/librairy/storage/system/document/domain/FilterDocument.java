package org.librairy.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Filter;
import org.librairy.model.domain.resources.Term;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="filters")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class FilterDocument extends Filter {

    @Override
    public Type getResourceType() {return Type.FILTER;}

    @Id
    private String uri;

}
