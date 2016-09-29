/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository;

import org.librairy.model.domain.LinkableElement;

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
public interface  Repository<L extends LinkableElement,T> {

    long count(T type);

    void save(L linkableElement);

    Boolean exists(T type, String uri);

    Optional<L> read(T type, String uri);

    Iterable<L> findAll(T type);

    Iterable<L> findBy(T resultType, String field, String value);

    Iterable<L> findFrom(T resultType, org.librairy.model.domain.resources.Resource.Type referenceType, String referenceURI);

    void deleteAll(T type);

    void delete(T type, String uri);

}
