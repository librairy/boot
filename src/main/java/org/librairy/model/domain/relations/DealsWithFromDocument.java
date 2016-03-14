package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class DealsWithFromDocument extends DealsWith {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return org.librairy.model.domain.resources.Resource.Type.DOCUMENT;
    }

    @Override
    public Type getType() {return Type.DEALS_WITH_FROM_DOCUMENT;}
}
