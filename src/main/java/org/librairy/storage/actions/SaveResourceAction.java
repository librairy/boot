package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.modules.RoutingKey;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.Helper;
import org.librairy.storage.executor.QueryTask;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveResourceAction.class);


    public SaveResourceAction(Helper helper, org.librairy.model.domain.resources.Resource resource){

        // initialize URI
        if (!resource.hasUri()){
            resource.setUri(helper.getUriGenerator().newFor(resource.getResourceType()));
        }

//        helper.getQueryExecutor().execute(new QueryTask(() -> {
            try{
                helper.getSession().clean();
                UnifiedTransaction transaction = helper.getSession().beginTransaction();

                LOG.debug("trying to save: " + resource);

                // Checking if exists
//                if (helper.getUnifiedColumnRepository().exists(resource.getResourceType(),resource.getUri())){
//                    LOG.warn("Resource already exists: " + resource);
//                    return;
//                }

                // column
                helper.getUnifiedColumnRepository().save(resource);
                // document
                helper.getUnifiedDocumentRepository().save(resource);
                // graph
                helper.getUnifiedNodeGraphRepository().save(resource);

                transaction.commit();

                LOG.debug("Resource Saved: " + resource);

                //Publish the event
                helper.getEventBus().post(Event.from(ResourceUtils.map(resource, Resource.class)), RoutingKey.of(resource.getResourceType(), Resource.State.CREATED));
            }catch (Exception e){
                throw new RuntimeException("Unexpected error while saving resource: "+resource.getUri(),e);
            }
//        }, 0, 0L));
    }

}
