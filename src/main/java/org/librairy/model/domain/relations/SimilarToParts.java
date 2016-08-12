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
@XmlRootElement(name = "similarTo")
@XmlAccessorType(XmlAccessType.FIELD)
public class SimilarToParts extends SimilarTo {

    @Override
    public org.librairy.model.domain.resources.Resource.Type getResourceType() {
        return org.librairy.model.domain.resources.Resource.Type.PART;
    }

    @Override
    public Type getType() {return Type.SIMILAR_TO_PARTS;}
}