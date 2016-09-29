/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.repository.edges;

import org.apache.commons.lang.WordUtils;
import org.librairy.model.domain.relations.Relation;
import org.librairy.storage.actions.ExecutionResult;
import org.librairy.storage.actions.RepeatableActionExecutor;
import org.librairy.storage.system.graph.cache.GraphCache;
import org.librairy.storage.system.graph.domain.edges.Edge;
import org.librairy.storage.system.graph.domain.nodes.Node;
import org.librairy.storage.system.graph.repository.Repository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
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
    public void save(org.librairy.model.domain.relations.Relation relation){
        performRetries(0, "save " + relation.getType() + "[" + relation + "]", () -> {

//            org.librairy.model.domain.resources.Resource snode = nodeFactory.repositoryOf(relation.getStartType()).findOneByUri(relation.getStartUri());
//            org.librairy.model.domain.resources.Resource enode = nodeFactory.repositoryOf(relation.getEndType()).findOneByUri(relation.getEndUri());

            org.librairy.model.domain.resources.Resource snode = cache.get(relation.getStartUri());
            org.librairy.model.domain.resources.Resource enode = cache.get(relation.getEndUri());

            if (snode == null || enode == null){
                throw new RuntimeException("One of nodes is null: ["+snode +"->"+enode+"]");
            }

            // Build the edge between nodes
            Edge edge = (Edge) org.librairy.model.utils.ResourceUtils.map(relation, factory.mappingOf(relation.getType()));
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
    public Boolean exists(org.librairy.model.domain.relations.Relation.Type type, String uri) {
        Optional<ExecutionResult> result = performRetries(0, "exists " + type + "[" + uri + "]", () ->
                factory.repositoryOf(type).findOneByUri(uri) != null);
        return (result.isPresent())? (Boolean) result.get().getResult() : Boolean.FALSE;
    }

    @Override
    public Optional<org.librairy.model.domain.relations.Relation> read(org.librairy.model.domain.relations.Relation.Type type, String uri) {
        Optional<ExecutionResult> result = performRetries(0, "read " + type + "[" + uri + "]", () -> {
            Optional<org.librairy.model.domain.relations.Relation> relation = Optional.empty();
            Edge edge = (Edge) factory.repositoryOf(type).findOneByUri(uri);
            if (edge != null) relation = Optional.of((org.librairy.model.domain.relations.Relation) org.librairy.model.utils.ResourceUtils.map(edge, org.librairy.model.domain.relations.Relation.classOf(type)));
            return relation;
        });
        return (result.isPresent())? (Optional<org.librairy.model.domain.relations.Relation>) result.get().getResult() :
                Optional.empty();
    }

    @Override
    public Iterable<org.librairy.model.domain.relations.Relation> findAll(org.librairy.model.domain.relations.Relation.Type type) {
        Optional<ExecutionResult> result = performRetries(0, "findAll " + type, () ->
                factory.repositoryOf(type).findAll());
        return (result.isPresent())? (Iterable<org.librairy.model.domain.relations.Relation>) result.get().getResult() :
                Collections.EMPTY_LIST;
    }

    public Iterable<org.librairy.model.domain.relations.Relation> findBetween(org.librairy.model.domain.relations.Relation.Type type, String startUri, String endUri) {
        Optional<ExecutionResult> result = performRetries(0, "finding " + type + " between [" + startUri+ "] and [" + endUri + "]", () -> {
            RelationGraphRepository repository = factory.repositoryOf(type);
            String methodName = "findByNodes";
            Method method = repository.getClass().getMethod(methodName, String.class, String.class);
            Iterable<org.librairy.model.domain.relations.Relation> relations = (Iterable<org.librairy.model.domain.relations.Relation>) method.invoke(repository, startUri, endUri);
//            Iterable<Relation> relations =  factory.repositoryOf(type).findByNodes(startUri,endUri);
            return relations;
        });
        return (result.isPresent())? (Iterable<org.librairy.model.domain.relations.Relation>) result.get().getResult() :
                Collections.EMPTY_LIST;
    }

    @Override
    public Iterable<org.librairy.model.domain.relations.Relation> findBy(org.librairy.model.domain.relations.Relation.Type result, String field, String value) {
        return find("findBy",result,value,field);
    }

    @Override
    public Iterable<org.librairy.model.domain.relations.Relation> findFrom(org.librairy.model.domain.relations.Relation.Type result, org.librairy.model.domain.resources.Resource.Type referenceType, String referenceURI) {
        return find("findBy",result,referenceURI,referenceType.key());
    }

    private Iterable<org.librairy.model.domain.relations.Relation> find(String prefix, org.librairy.model.domain.relations.Relation.Type resultType, String uri, String reference) {
        Optional<ExecutionResult> result = performRetries(0, prefix + " " + resultType + "[" + uri + "] and ref: " + reference, () -> {
            RelationGraphRepository repository = factory.repositoryOf(resultType);
            String methodName = prefix + WordUtils.capitalize(reference.toLowerCase());
            Method method = repository.getClass().getMethod(methodName, String.class);
            Iterable<org.librairy.model.domain.relations.Relation> resources = (Iterable<org.librairy.model.domain.relations.Relation>) method.invoke(repository, uri);
            return resources;
        });
        return (result.isPresent())? (Iterable<org.librairy.model.domain.relations.Relation>) result.get().getResult() :
                Collections.EMPTY_LIST;
    }

    @Override
    public void deleteAll(org.librairy.model.domain.relations.Relation.Type type) {
        performRetries(0,"delete all "+ type, () -> {
            factory.repositoryOf(type).deleteAll();
            return 1;
        });
    }

    @Override
    public void delete(org.librairy.model.domain.relations.Relation.Type type, String uri) {
        performRetries(0,"delete " + type + "["+uri+"]", () -> {
            Edge relation = factory.repositoryOf(type).findOneByUri(uri);
            //if (relation != null) factory.repositoryOf(type).delete(factory.mappingOf(type).cast(relation) );
            if (relation != null) factory.repositoryOf(type).delete(relation.getId());
            return 1;
        });
    }
}
