package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class SimilarToParts extends SimilarTo {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getResourceType() {
        return org.librairy.model.domain.resources.Resource.Type.PART;
    }

    @Override
    public Type getType() {return Type.SIMILAR_TO_PARTS;}
}
