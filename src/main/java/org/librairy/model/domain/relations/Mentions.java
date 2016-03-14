package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class Mentions extends Relation{

    @Getter @Setter
    private Long times = 0L;

    @Override
    public org.librairy.model.domain.resources.Resource.Type getEndType() {
        return org.librairy.model.domain.resources.Resource.Type.WORD;
    }

    @Override
    public Double getWeight() {
        return Double.valueOf(times);
    }
}
