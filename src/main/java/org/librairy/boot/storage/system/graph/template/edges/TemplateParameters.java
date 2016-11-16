/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.template.edges;

import lombok.Getter;
import org.librairy.boot.model.domain.relations.Relation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 09/03/16.
 */
public class TemplateParameters {

    @Getter
    private final Relation relation;

    @Getter
    Map<String,Object> params;

    Map<String,Integer> labels;

    private Integer counter;

    public TemplateParameters(Relation relation){
        params = new HashMap<>();
        labels = new HashMap<>();
        counter = 5;

        this.relation = relation;
        params.put("0",relation.getStartUri());
        params.put("1",relation.getEndUri());
        params.put("2",relation.getUri());
        params.put("3",relation.getCreationTime());
        params.put("4",relation.getWeight());
    }


    public TemplateParameters add(String label, Object value){
        params.put(String.valueOf(counter),value);
        labels.put(label,counter);
        counter++;
        return this;
    }


    public String toExpression(){
        return labels.keySet().stream().map(key -> new StringBuilder().append(key).append(" : ").append("{").append(labels.get(key)).append("}")).collect(Collectors.joining(", "));
    }

}
