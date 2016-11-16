/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.document.repository;

import org.apache.commons.lang.WordUtils;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.storage.exception.RepositoryNotFound;
import org.librairy.boot.storage.system.graph.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class UnifiedDocumentRepository implements Repository<Resource,Resource.Type> {

    @Autowired
    UnifiedDocumentRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedDocumentRepository.class);

    @Override
    public long count(Resource.Type type){
        return factory.repositoryOf(type).count();
    }

    @Override
    public void save(Resource resource){
        try {
            factory.repositoryOf(resource.getResourceType()).save(ResourceUtils.map(resource, factory.mappingOf(resource.getResourceType())));
        } catch (RepositoryNotFound e){
            LOG.debug(e.getMessage());
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public Boolean exists(Resource.Type type, String uri){
        try{
            return factory.repositoryOf(type).exists(uri);
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Resource> read(Resource.Type type, String uri) {
        Optional<Resource> result = Optional.empty();
        try{
            Resource document = (Resource) factory.repositoryOf(type).findOne(uri);
            if (document != null) result = Optional.of((Resource) ResourceUtils.map(document, Resource.classOf(type)));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public Iterable<Resource> findAll(Resource.Type type) {
        try{
            return factory.repositoryOf(type).findAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Iterable<Resource> findBy(Resource.Type resultType, String field, String value) {
        return find("findBy",resultType,field,value);
    }

    @Override
    public Iterable<Resource> findFrom(Resource.Type resultType, Resource.Type referenceType, String referenceURI) {
        return find("findBy",resultType,referenceType.key(),referenceURI);
    }

    private Iterable<Resource> find(String prefix, Resource.Type result, String reference, String value) {
        try{
            BaseDocumentRepository repository = factory.repositoryOf(result);

            String methodName = prefix+ WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<Resource> resources = (Iterable<Resource>) method.invoke(repository, value);
            return resources;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.warn("No such method to find: " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }


    @Override
    public void delete(Resource.Type type, String uri){
        try{
            factory.repositoryOf(type).delete(uri);
        } catch (RepositoryNotFound e){
            LOG.debug(e.getMessage());
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public void deleteAll(Resource.Type type) {
        try{
            factory.repositoryOf(type).deleteAll();
        } catch (RepositoryNotFound e){
            LOG.debug(e.getMessage());
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }


}
