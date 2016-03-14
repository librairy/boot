package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class SimilarToItems extends SimilarTo {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getResourceType() {
        return org.librairy.model.domain.resources.Resource.Type.ITEM;
    }

    @Override
    public Relation.Type getType() {return Relation.Type.SIMILAR_TO_ITEMS;}
}
