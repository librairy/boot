/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.modules;

import lombok.Data;

/**
 * Created by cbadenes on 26/11/15.
 */
@Data
public class BindingKey {

    String key;

    String group;


    private BindingKey(String key, String group){
        this.key = key;
        this.group = group;
    }

    public static BindingKey of(RoutingKey routingKey, String groupKey){
        return new BindingKey(routingKey.key,groupKey);
    }

    public static BindingKey of(String key, String group){
        return new BindingKey(key,group);
    }

}
