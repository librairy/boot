package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Contains extends Relation {

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.DOMAIN;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOCUMENT;
    }

    @Override
    public Double getWeight() {
        return 1.0;
    }

    @Override
    public Type getType() {return Type.CONTAINS;}
}
