/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system;

import org.librairy.boot.model.domain.LinkableElement;
import org.librairy.boot.model.domain.resources.Resource;

import java.util.Optional;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public interface  Repository<L extends LinkableElement,T> {

    long count(T type);

    void save(L linkableElement);

    Boolean exists(T type, String uri);

    Optional<L> read(T type, String uri);

    Iterable<L> findAll(T type);

    Iterable<L> findBy(T resultType, String field, String value);

    Iterable<L> findFrom(T resultType, Resource.Type referenceType, String referenceURI);

    void deleteAll(T type);

    void delete(T type, String uri);

}