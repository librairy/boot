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
import org.librairy.storage.session.UnifiedTransaction;
import org.librairy.storage.utils.RelationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SearchResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResourceAction.class);

    private final Helper helper;
    private final org.librairy.model.domain.resources.Resource.Type type;

    public SearchResourceAction(Helper helper, org.librairy.model.domain.resources.Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Find all resources
     */
    public List<Resource> all(){
        LOG.debug("Finding " + type.name() + "s");
        List<Resource> resources = new ArrayList<>();
        try{
                //TODO remove it
//
//            if (helper.getTemplateFactory().handle(type)){
//                helper.getTemplateFactory().of(type).findAll().forEach(x -> resources.add((Resource) ResourceUtils.map(x, Resource.classOf(type))));
//            }else{
//                helper.getUnifiedNodeGraphRepository().findAll(type).forEach(x -> resources.add(x));
//            }

            helper.getUnifiedColumnRepository().findAll(type).forEach(x -> resources.add((Resource) ResourceUtils.map(x, Resource.classOf(type))));

            LOG.trace(type.name() + "s: " + resources);
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return resources;
    }

    /**
     * Find resources attached to other resource (directly or indirectly)
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public List<Resource> from(Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + type.name() + "s in " + referenceType + ": " + referenceURI);
        List<Resource> resources = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            //TODO remove it
//            if (helper.getTemplateFactory().handle(type)){
//                helper.getTemplateFactory().of(type).findFrom(referenceType,referenceURI).forEach(x -> resources.add((Resource) ResourceUtils.map(x, Resource.classOf(type))));
//            }else{
//                helper.getUnifiedNodeGraphRepository().findFrom(type, referenceType,referenceURI).forEach(x -> resources.add((Resource) ResourceUtils.map(x, Resource.classOf(type))));
//            }

            Relation.Type relType = RelationUtils.getRelationBetween(type,referenceType);
            String field = RelationUtils.getFieldFromRelation(relType, referenceType);
            helper.getUnifiedColumnRepository().findBy(relType, field, referenceURI );

            transaction.commit();
            LOG.debug("In "+referenceType+": " + referenceURI + " found: ["+type + "]: " + resources);
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s in " + referenceType + ": " + referenceURI,e);
        }
        return resources;
    }



    /**
     * Find resources by a field value
     * @param field
     * @param value
     * @return
     */
    public List<Resource> by(String field, String value){
        LOG.debug("Finding " + type.name() + "s");
        List<Resource> resources = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedColumnRepository().findBy(type, field,value).forEach(x -> resources.add((Resource) ResourceUtils.map(x, Resource.classOf(type))));

            transaction.commit();
            LOG.debug("By "+field+": '" + value+ "' found: ["+type + "]: " + resources);
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return resources;
    }

}
