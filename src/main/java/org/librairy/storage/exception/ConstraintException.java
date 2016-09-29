/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.exception;

/**
 * Created on 05/09/16:
 *
 * @author cbadenes
 */
public class ConstraintException extends RuntimeException {

    public ConstraintException(String msg){
        super(msg);
    }
}
