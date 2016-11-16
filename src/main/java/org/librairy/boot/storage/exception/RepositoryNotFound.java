/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.exception;

/**
 * Created by cbadenes on 12/02/16.
 */
public class RepositoryNotFound extends IllegalArgumentException {

    public RepositoryNotFound(String msg){
        super(msg);
    }
}
