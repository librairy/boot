/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Data
@EqualsAndHashCode
@ToString
public class TopicDescription {

    String domainId;

    String id;

    List<WordDescription> words = new ArrayList<>();

    public void add(WordDescription wordDescription){
        this.words.add(wordDescription);
    }

    public List<WordDescription> getWords(){
        return words.stream().sorted((a,b) -> -a.getScore().compareTo(b.getScore())).collect(Collectors.toList());
    }
}
