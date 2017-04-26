/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column.repository;

import org.apache.commons.lang.WordUtils;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.storage.system.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class UnifiedColumnRepository implements Repository<Resource,Resource.Type> {

    @Autowired
    UnifiedColumnRepositoryFactory factory;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedColumnRepository.class);

    @Override
    public long count(Resource.Type type){
        return factory.repositoryOf(type).count();
    }

    public long count(Relation.Type type){
        return factory.repositoryOf(type).count();
    }

    @Override
    public void save(Resource resource){
        try{
            factory.repositoryOf(resource.getResourceType()).save(ResourceUtils.map(resource, factory.mappingOf(resource.getResourceType())));
            LOG.debug("Resource: " + resource + " saved");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void save(Relation resource){
        try{
            factory.repositoryOf(resource.getType()).save(ResourceUtils.map(resource, factory.mappingOf(resource
                    .getType())));
            LOG.debug("Relation: " + resource + " saved");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        }
    }

    public void save(Relation.Type type, Iterable relations){
        try{
            factory.repositoryOf(type).save(relations);
            LOG.debug("Relations saved");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    @Override
    public Boolean exists(Resource.Type type, String uri){

        try{
            return factory.repositoryOf(type).exists(BasicMapId.id(ResourceUtils.URI, uri));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return false;
    }


    public Boolean exists(Relation.Type type, String uri){

        try{
            return factory.repositoryOf(type).exists(BasicMapId.id(ResourceUtils.URI, uri));
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Resource> read(Resource.Type type, String uri){
        Optional<Resource> result = Optional.empty();
        try{
            Resource column = (Resource) factory.repositoryOf(type).findOne(BasicMapId.id(ResourceUtils.URI, uri));
            if (column != null) result = Optional.of((Resource) ResourceUtils.map(column, Resource.classOf(type)));
            LOG.debug("Resource read: " + column );
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }


    public Optional<Relation> read(Relation.Type type, String uri){
        Optional<Relation> result = Optional.empty();
        try{
            Relation column = (Relation) factory.repositoryOf(type).findOne(BasicMapId.id(ResourceUtils.URI, uri));
            if (column != null) result = Optional.of((Relation) ResourceUtils.map(column, Relation.classOf(type)));
            LOG.debug("Relation read: " + column );
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
        return result;
    }

    @Override
    public Iterable<Resource> findAll(Resource.Type type){
        try{
            return factory.repositoryOf(type).findAll();
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }


    public Iterable<Relation> findAll(Relation.Type type){
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

    public Iterable<Relation> findBy(Relation.Type resultType, String field, String value) {
        return find("findBy",resultType,field,value);
    }

    @Override
    public Iterable<Resource> findFrom(Resource.Type resultType, Resource.Type referenceType, String referenceURI) {
        return find("findBy",resultType,referenceType.key(),referenceURI);
    }

    private Method findMethod(String prefix,String reference,Class repositoryClass) throws NoSuchMethodException {
        String methodName = prefix+ WordUtils.capitalize(reference.toLowerCase());
        return repositoryClass.getMethod(methodName, String.class);
    }

    private Iterable<Resource> find(String prefix, Resource.Type result,String reference,String value) {
        try{
            BaseColumnRepository repository = factory.repositoryOf(result);
            Method method                   = findMethod(prefix,reference,repository.getClass());
            Iterable<Resource> resources    = (Iterable<Resource>) method.invoke(repository, value);
            return resources;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.warn("No such method to find: " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    private Iterable<Relation> find(String prefix, Relation.Type result,String reference,String value) {
        try{
            BaseColumnRepository repository = factory.repositoryOf(result);
            Method method                   = findMethod(prefix,reference,repository.getClass());
            Iterable<Relation> resources    = (Iterable<Relation>) method.invoke(repository, value);
            return resources;
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.warn("No such method to find: " + e.getMessage(),e);
        }
        return Collections.EMPTY_LIST;
    }

    public void delete(Resource.Type type, String uri){
        try{
            factory.repositoryOf(type).delete(BasicMapId.id(ResourceUtils.URI, uri));
            LOG.debug("Resource: " + uri + " deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void delete(Resource.Type type,Iterable<Resource> relations){
        try{
            factory.repositoryOf(type).delete(relations);
            LOG.debug("Resources deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void delete(Relation.Type type, String uri){
        try{
            factory.repositoryOf(type).delete(BasicMapId.id(ResourceUtils.URI, uri));
            LOG.debug("Resource: " + uri + " deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void delete(Relation.Type type,Iterable<Relation> relations){
        try{
            factory.repositoryOf(type).delete(relations);
            LOG.debug("Relations deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }


    public void deleteAll(Resource.Type type){
        try{
            factory.repositoryOf(type).deleteAll();
            LOG.debug("All " + type.route() + " have been deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }

    public void deleteAll(Relation.Type type){
        try{
            factory.repositoryOf(type).deleteAll();
            LOG.debug("All " + type.route() + " have been deleted");
        }catch (RuntimeException e){
            LOG.warn(e.getMessage());
        }
    }







}
