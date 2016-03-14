package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Bundles extends Relation {

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.DOCUMENT;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.ITEM;
    }

    @Override
    public Double getWeight() {
        return 1.0;
    }

    @Override
    public Type getType() {return Type.BUNDLES;}
}
