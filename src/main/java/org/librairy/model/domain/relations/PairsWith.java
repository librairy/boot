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
public class PairsWith extends Relation {

    @Getter
    @Setter
    private Double weight;

    @Getter
    @Setter
    private String domain;

    @Override
    public Resource.Type getStartType() {
        return Resource.Type.WORD;
    }

    @Override
    public Resource.Type getEndType() {
        return Resource.Type.WORD;
    }

    @Override
    public Type getType() {return Type.PAIRS_WITH;}
}
