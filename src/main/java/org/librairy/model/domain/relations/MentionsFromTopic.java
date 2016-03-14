package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class MentionsFromTopic extends Mentions {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return org.librairy.model.domain.resources.Resource.Type.TOPIC;
    }

    @Override
    public Type getType() {return Type.MENTIONS_FROM_TOPIC;}
}
