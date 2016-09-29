/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.actions;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by cbadenes on 12/02/16.
 */
@FunctionalInterface
public interface RepeatableAction<T> {

    public abstract T run() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
