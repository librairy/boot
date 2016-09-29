/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.document.repository;

import org.apache.commons.lang.WordUtils;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.exception.RepositoryNotFound;
import org.librairy.storage.system.graph.repository.Repository;
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
    public void save(org.librairy.model.domain.resources.Resource resource){
        try {
            factory.repositoryOf(resource.getResourceType()).save(ResourceUtils.map(resource, factory.mappingOf(resource.getResourceType())));
        } catch (RepositoryNotFound e){
            LOG.debug(e.getMessage());
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public Boolean exists(org.librairy.model.domain.resources.Resource.Type type, String uri){
        try{
            return factory.repositoryOf(type).exists(uri);
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<org.librairy.model.domain.resources.Resource> read(org.librairy.model.domain.resources.Resource.Type type, String uri) {
        Optional<org.librairy.model.domain.resources.Resource> result = Optional.empty();
        try{
            org.librairy.model.domain.resources.Resource document = (org.librairy.model.domain.resources.Resource) factory.repositoryOf(type).findOne(uri);
            if (document != null) result = Optional.of((org.librairy.model.domain.resources.Resource) ResourceUtils.map(document, org.librairy.model.domain.resources.Resource.classOf(type)));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public Iterable<org.librairy.model.domain.resources.Resource> findAll(org.librairy.model.domain.resources.Resource.Type type) {
        try{
            return factory.repositoryOf(type).findAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Iterable<org.librairy.model.domain.resources.Resource> findBy(org.librairy.model.domain.resources.Resource.Type resultType, String field, String value) {
        return find("findBy",resultType,field,value);
    }

    @Override
    public Iterable<org.librairy.model.domain.resources.Resource> findFrom(org.librairy.model.domain.resources.Resource.Type resultType, org.librairy.model.domain.resources.Resource.Type referenceType, String referenceURI) {
        return find("findBy",resultType,referenceType.key(),referenceURI);
    }

    private Iterable<org.librairy.model.domain.resources.Resource> find(String prefix, org.librairy.model.domain.resources.Resource.Type result, String reference, String value) {
        try{
            BaseDocumentRepository repository = factory.repositoryOf(result);

            String methodName = prefix+ WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<org.librairy.model.domain.resources.Resource> resources = (Iterable<org.librairy.model.domain.resources.Resource>) method.invoke(repository, value);
            return resources;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.warn("No such method to find: " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }


    @Override
    public void delete(org.librairy.model.domain.resources.Resource.Type type, String uri){
        try{
            factory.repositoryOf(type).delete(uri);
        } catch (RepositoryNotFound e){
            LOG.debug(e.getMessage());
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public void deleteAll(org.librairy.model.domain.resources.Resource.Type type) {
        try{
            factory.repositoryOf(type).deleteAll();
        } catch (RepositoryNotFound e){
            LOG.debug(e.getMessage());
        } catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }


}
