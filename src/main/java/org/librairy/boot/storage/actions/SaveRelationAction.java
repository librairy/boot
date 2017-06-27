/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.actions;

import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.storage.Helper;
import org.librairy.boot.storage.exception.DataNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.librairy.boot.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveRelationAction.class);


    public SaveRelationAction(Helper helper, Relation relation){

        // setting URI
        StringBuilder contentBuilder = new StringBuilder().append(relation.getStartUri()).append(relation.getEndUri());
        if (!relation.hasUri()) {
            switch (relation.getType()) {
                // 1 to 1 relations
                case CONTAINS_TO_ITEM:
                    helper.getCounterDao().increment(relation.getStartUri(), Resource.Type.ITEM.route());
                    break;
                case CONTAINS_TO_PART:
                    // increment counter
                    helper.getCounterDao().increment(relation.getStartUri(), Resource.Type.PART.route());
                    break;
                case DEALS_WITH_FROM_ITEM:
                case DEALS_WITH_FROM_PART:
                case DESCRIBES:
                    break;
                case SIMILAR_TO_ITEMS:
                    contentBuilder.append(relation.asSimilarToItems().getDomain());
                    break;
                case SIMILAR_TO_PARTS:
                    contentBuilder.append(relation.asSimilarToParts().getDomain());
                    break;
                default:
                    LOG.warn("Relation not handled,  set default URI for: " + relation.getType());
            }
            relation.setUri(helper.getUriGenerator().basedOnContent(relation.getType(), contentBuilder.toString()));
        }

        // increment counter
        helper.getCounterDao().increment(relation.getType().route());

        try{
            LOG.debug("trying to save :" + relation);

            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // TODO remove it
            // Graph Database (Neo4j bug avoid update values)
//            if (helper.getUnifiedColumnRepository().exists(relation.getType(),relation.getUri())){
//                helper.getTemplateFactory().of(relation.getType()).delete(relation.getUri());
//            }
//
//            helper.getTemplateFactory().of(relation.getType()).save(relation);

            // Column Database (save or update values)
            helper.getUnifiedColumnRepository().save(relation);

            transaction.commit();

            LOG.debug("Relation Saved: " + relation);

            //Publish the event
            helper.getEventBus().post(Event.from(ResourceUtils.map(relation,Relation.class)), RoutingKey.of(relation.getType(), Relation.State.CREATED));
        }catch (Exception e){
            throw new RuntimeException("Unexpected error while saving relation: "+relation,e);
        }

    }

}
