/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.actions;

import lombok.Data;

/**
 * Created on 31/08/16:
 *
 * @author cbadenes
 */
@Data
public class ExecutionResult {

    private Integer retries;

    private Object result;

    public ExecutionResult(Integer retries, Object result){
        this.retries = retries;
        this.result = result;
    }

}
