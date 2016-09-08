package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.modules.RoutingKey;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.Helper;
import org.librairy.storage.session.UnifiedTransaction;
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
                case AGGREGATES:
                case APPEARED_IN:
                case BUNDLES:
                case COMPOSES:
                case CONTAINS:
                case DEALS_WITH_FROM_DOCUMENT:
                case DEALS_WITH_FROM_ITEM:
                case DEALS_WITH_FROM_PART:
                case DESCRIBES:
                case EMBEDDED_IN:
                case EMERGES_IN:
                case MENTIONS_FROM_TERM:
                case MENTIONS_FROM_TOPIC:
                case PROVIDES:
                    break;
                // n to n relations
                case SIMILAR_TO_DOCUMENTS:
                    contentBuilder.append(relation.asSimilarToDocuments().getDomain());
                    break;
                case SIMILAR_TO_ITEMS:
                    contentBuilder.append(relation.asSimilarToItems().getDomain());
                    break;
                case SIMILAR_TO_PARTS:
                    contentBuilder.append(relation.asSimilarToParts().getDomain());
                    break;
                case PAIRS_WITH:
                    contentBuilder.append(relation.asPairsWith().getDomain());
                    break;
                case HYPERNYM_OF:
                    contentBuilder.append(relation.asHypernymOf().getDomain());
                    break;
                default:
                    LOG.warn("Relation not handled,  set default URI for: " + relation.getType());
            }
            relation.setUri(helper.getUriGenerator().basedOnContent(relation.getType(), contentBuilder.toString()));
        }


        try{
            LOG.debug("trying to save :" + relation);

            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // Graph Database (Neo4j bug avoid update values)
            helper.getTemplateFactory().of(relation.getType()).save(relation);

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
