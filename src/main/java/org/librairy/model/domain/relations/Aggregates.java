package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Aggregates extends Relation {

    @Getter
    @Setter
    private Double weight;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.DOCUMENT;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.DOCUMENT;
    }

    @Override
    public Type getType() {return Type.AGGREGATES;}
}
