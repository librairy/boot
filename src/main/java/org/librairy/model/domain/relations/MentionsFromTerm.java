package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@XmlRootElement(name = "mentions")
@XmlAccessorType(XmlAccessType.FIELD)
public class MentionsFromTerm extends Mentions {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getStartType() {
        return org.librairy.model.domain.resources.Resource.Type.TERM;
    }

    @Override
    public Type getType() {return Type.MENTIONS_FROM_TERM;}
}