package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.modules.RoutingKey;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.Helper;
import org.librairy.storage.executor.QueryTask;
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
            relation.setUri(helper.getUriGenerator().newFor(relation.getType()));
        }

        Random random = new Random();
        helper.getQueryExecutor().execute(new QueryTask(() -> {
            try{
                LOG.debug("trying to save :" + relation);

                helper.getSession().clean();
                UnifiedTransaction transaction = helper.getSession().beginTransaction();



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
                LOG.error("Unexpected error while saving relation: "+relation,e);
            }
        }, 10, 50L + random.nextInt(100)));



    }

}
