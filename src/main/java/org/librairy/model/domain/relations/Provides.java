package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Provides extends Relation {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return org.librairy.model.domain.resources.Resource.Type.SOURCE;
    }

    @Override
    public org.librairy.model.domain.resources.Resource.Type getEndType() {
        return org.librairy.model.domain.resources.Resource.Type.DOCUMENT;
    }

    @Override
    public Double getWeight() {
        return 1.0;
    }

    @Override
    public Type getType() {return Type.PROVIDES;}

}
