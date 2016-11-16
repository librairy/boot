/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.graph.repository.edges;

import org.apache.commons.lang.WordUtils;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.storage.system.graph.domain.edges.Edge;
import org.librairy.boot.storage.system.graph.repository.Repository;
import org.librairy.boot.storage.actions.ExecutionResult;
import org.librairy.boot.storage.actions.RepeatableActionExecutor;
import org.librairy.boot.storage.system.graph.cache.GraphCache;
import org.librairy.boot.storage.system.graph.domain.nodes.Node;
import org.librairy.boot.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by cbadenes on 02/02/16.
 */
@Component
public class UnifiedEdgeGraphRepository extends RepeatableActionExecutor implements Repository<Relation,Relation.Type> {

    @Autowired
    UnifiedNodeGraphRepositoryFactory nodeFactory;

    @Autowired
    UnifiedEdgeGraphRepositoryFactory factory;

    @Autowired
    Session session;

    @Autowired
    GraphCache cache;

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedEdgeGraphRepository.class);

    @Override
    public long count(Relation.Type type){
        return factory.repositoryOf(type).count();
    }

    @Override
    public void save(Relation relation){
        performRetries(0, "save " + relation.getType() + "[" + relation + "]", () -> {

//            Resource snode = nodeFactory.repositoryOf(relation.getStartType()).findOneByUri(relation.getStartUri());
//            Resource enode = nodeFactory.repositoryOf(relation.getEndType()).findOneByUri(relation.getEndUri());

            Resource snode = cache.get(relation.getStartUri());
            Resource enode = cache.get(relation.getEndUri());

            if (snode == null || enode == null){
                throw new RuntimeException("One of nodes is null: ["+snode +"->"+enode+"]");
            }

            // Build the edge between nodes
            Edge edge = (Edge) ResourceUtils.map(relation, factory.mappingOf(relation.getType()));
            edge.setStart((Node) snode);
            edge.setEndNode((Node) enode);

            factory.repositoryOf(relation.getType()).save(edge);
            return 1;
        });
    }

    public void save(Relation.Type type, Iterable<Edge> relations){
        factory.repositoryOf(type).save(relations);
    }

    @Override
    public Boolean exists(Relation.Type type, String uri) {
        Optional<ExecutionResult> result = performRetries(0, "exists " + type + "[" + uri + "]", () ->
                factory.repositoryOf(type).findOneByUri(uri) != null);
        return (result.isPresent())? (Boolean) result.get().getResult() : Boolean.FALSE;
    }

    @Override
    public Optional<Relation> read(Relation.Type type, String uri) {
        Optional<ExecutionResult> result = performRetries(0, "read " + type + "[" + uri + "]", () -> {
            Optional<Relation> relation = Optional.empty();
            Edge edge = (Edge) factory.repositoryOf(type).findOneByUri(uri);
            if (edge != null) relation = Optional.of((Relation) ResourceUtils.map(edge, Relation.classOf(type)));
            return relation;
        });
        return (result.isPresent())? (Optional<Relation>) result.get().getResult() :
                Optional.empty();
    }

    @Override
    public Iterable<Relation> findAll(Relation.Type type) {
        Optional<ExecutionResult> result = performRetries(0, "findAll " + type, () ->
                factory.repositoryOf(type).findAll());
        return (result.isPresent())? (Iterable<Relation>) result.get().getResult() :
                Collections.EMPTY_LIST;
    }

    public Iterable<Relation> findBetween(Relation.Type type, String startUri, String endUri) {
        Optional<ExecutionResult> result = performRetries(0, "finding " + type + " between [" + startUri+ "] and [" + endUri + "]", () -> {
            RelationGraphRepository repository = factory.repositoryOf(type);
            String methodName = "findByNodes";
            Method method = repository.getClass().getMethod(methodName, String.class, String.class);
            Iterable<Relation> relations = (Iterable<Relation>) method.invoke(repository, startUri, endUri);
//            Iterable<Relation> relations =  factory.repositoryOf(type).findByNodes(startUri,endUri);
            return relations;
        });
        return (result.isPresent())? (Iterable<Relation>) result.get().getResult() :
                Collections.EMPTY_LIST;
    }

    @Override
    public Iterable<Relation> findBy(Relation.Type result, String field, String value) {
        return find("findBy",result,value,field);
    }

    @Override
    public Iterable<Relation> findFrom(Relation.Type result, Resource.Type referenceType, String referenceURI) {
        return find("findBy",result,referenceURI,referenceType.key());
    }

    private Iterable<Relation> find(String prefix, Relation.Type resultType, String uri, String reference) {
        Optional<ExecutionResult> result = performRetries(0, prefix + " " + resultType + "[" + uri + "] and ref: " + reference, () -> {
            RelationGraphRepository repository = factory.repositoryOf(resultType);
            String methodName = prefix + WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<Relation> resources = (Iterable<Relation>) method.invoke(repository, uri);
            return resources;
        });
        return (result.isPresent())? (Iterable<Relation>) result.get().getResult() :
                Collections.EMPTY_LIST;
    }

    @Override
    public void deleteAll(Relation.Type type) {
        performRetries(0,"delete all "+ type, () -> {
            factory.repositoryOf(type).deleteAll();
            return 1;
        });
    }

    @Override
    public void delete(Relation.Type type, String uri) {
        performRetries(0,"delete " + type + "["+uri+"]", () -> {
            Edge relation = factory.repositoryOf(type).findOneByUri(uri);
            //if (relation != null) factory.repositoryOf(type).delete(factory.mappingOf(type).cast(relation) );
            if (relation != null) factory.repositoryOf(type).delete(relation.getId());
            return 1;
        });
    }
}
