package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.librairy.storage.generator.URIGenerator;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class SimilarTo extends Relation{

    @Getter @Setter
    private Double weight;

    @Getter @Setter
    private String domain;

    @Override
    public String getUriComposition() {
        return super.getUriComposition()+"-"+ URIGenerator.retrieveId(domain);
    }

    public abstract org.librairy.model.domain.resources.Resource.Type getResourceType();

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return getResourceType();
    }

    @Override
    public org.librairy.model.domain.resources.Resource.Type getEndType() {
        return getResourceType();
    }

}
