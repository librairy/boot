package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.modules.RoutingKey;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.Helper;
import org.librairy.storage.executor.QueryTask;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveRelationAction.class);


    public SaveRelationAction(Helper helper, Relation relation){

        // initialize URI
        if (!relation.hasUri()){

            String startId  = URIGenerator.retrieveId(relation.getStartUri());
            String endId    = URIGenerator.retrieveId(relation.getEndUri());

            relation.setUri(helper.getUriGenerator().from(relation.getType(),startId+"-"+endId));
        }

        try{
            LOG.debug("trying to save :" + relation);

            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // Column Database
            helper.getUnifiedColumnRepository().save(relation);


            // Graph Database
            if (helper.getTemplateFactory().handle(relation.getType())){
                helper.getTemplateFactory().of(relation.getType()).save(relation);
            }else{
                helper.getUnifiedEdgeGraphRepository().save(relation);
            }

            transaction.commit();

            LOG.debug("Relation Saved: " + relation);

            //Publish the event
            helper.getEventBus().post(Event.from(ResourceUtils.map(relation,Relation.class)), RoutingKey.of(relation.getType(), Relation.State.CREATED));
        }catch (Exception e){
            throw new RuntimeException("Unexpected error while saving relation: "+relation,e);
        }

    }

}
