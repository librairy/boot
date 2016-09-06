package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@XmlRootElement(name = "aggregates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Aggregates extends Relation {

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
