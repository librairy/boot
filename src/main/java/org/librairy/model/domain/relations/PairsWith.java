package org.librairy.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.generator.URIGenerator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cbadenes on 17/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@XmlRootElement(name = "pairsWith")
@XmlAccessorType(XmlAccessType.FIELD)
public class PairsWith extends Relation {

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
