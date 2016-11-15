/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.actions;

import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.Helper;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.session.UnifiedTransaction;
import org.librairy.storage.utils.RelationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SearchRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRelationAction.class);

    private final Helper helper;
    private final Relation.Type type;

    public SearchRelationAction(Helper helper, Relation.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Find all relations
     */
    public List<Relation> all(){
        LOG.debug("Finding " + type.name() + "s");
        List<Relation> relations = new ArrayList<>();
        try{

            // TODO remove it
//            if (helper.getTemplateFactory().handle(type)){
//                helper.getTemplateFactory().of(type).findAll().forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));
//            }else{
//                helper.getUnifiedEdgeGraphRepository().findAll(type).forEach(x -> relations.add(Relation.class.cast(x)));
//            }

            helper.getUnifiedColumnRepository().findAll(type).forEach(x -> relations.add(Relation.class.cast(x)));

            LOG.trace(type.name() + "s: " + relations);
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return relations;
    }


    /**
     * Find relation attached to other resource (directly or indirectly)
     * @param startUri
     * @param endUri
     * @return
     */
    public List<Relation> btw(String startUri, String endUri){
        LOG.debug("Finding " + type.name() + "s between " + startUri + " and " + endUri);
        List<Relation> relations = new ArrayList<>();
        try{
//            helper.getSession().clean();
//            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // TODO remove it
//            if (helper.getTemplateFactory().handle(type)){
//                helper.getTemplateFactory().of(type).findOne(startUri,endUri).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));
//            }else{
//                helper.getUnifiedEdgeGraphRepository().findBetween(type, startUri, endUri).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));
//            }

            throw new RuntimeException("Method not implemented");

//            transaction.commit();
//            return relations;
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s between " + startUri+ " and " + endUri,e);
        }
        return relations;
    }

    /**
     * Find relation attached to other resource (directly or indirectly)
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public List<Relation> from(org.librairy.model.domain.resources.Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + type.name() + "s in " + referenceType + ": " + referenceURI);
        List<Relation> relations = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            String field = RelationUtils.getFieldFromRelation(type, referenceType);

            helper.getUnifiedColumnRepository().findBy(type, field, referenceURI).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));


            //TODO remove it
//            if (helper.getTemplateFactory().handle(type)){
//                helper.getTemplateFactory().of(type).findIn(referenceType,referenceURI).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));
//            }else{
//                helper.getUnifiedEdgeGraphRepository().findFrom(type, referenceType, referenceURI).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));
//            }

            transaction.commit();
            return relations;
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s in " + referenceType + ": " + referenceURI,e);
        }
        return relations;
    }


    /**
     * Find relations by a field value
     * @param field
     * @param value
     * @return
     */
    public List<Relation> by(String field, String value){
        LOG.debug("Finding " + type.name() + "s");
        List<Relation> relations = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // TODO remove it
//            helper.getUnifiedEdgeGraphRepository().findBy(type, field, value).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));

            helper.getUnifiedColumnRepository().findBy(type, field, value).forEach(x -> relations.add((Relation) ResourceUtils.map(x,Relation.classOf(type))));

            transaction.commit();
            return relations;
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return relations;
    }

}
