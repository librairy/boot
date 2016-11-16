/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cbadenes on 16/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@XmlRootElement(name = "provenance")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ProvenanceRelation extends Relation {

    @Getter @Setter
    protected Map<String, Double> provenances = new HashMap<>();

    @Override
    public Double getWeight() {
        if ((provenances != null) && (!provenances.isEmpty())) {
            return provenances.values().stream().mapToDouble(x -> x.doubleValue()).average().getAsDouble();
        }
        else{
            return weight;
        }
    }

    public void add(String provenance, double relationProbability) {
        this.provenances.put(provenance, relationProbability);
    }

    public void addAll(Map<String, Double> provenance ){
        provenances.putAll(provenance);
    }

}
