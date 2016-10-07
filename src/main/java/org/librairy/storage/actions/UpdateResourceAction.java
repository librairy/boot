/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.actions;

import com.google.common.base.Strings;
import org.librairy.model.Event;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.modules.RoutingKey;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.model.utils.TimeUtils;
import org.librairy.storage.Helper;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class UpdateResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateResourceAction.class);


    public UpdateResourceAction(Helper helper, Resource resource){

        // initialize URI
        if (!resource.hasUri()){
            throw new RuntimeException("Not URI defined for resource: " + resource);
        }


        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            LOG.debug("trying to save: " + resource);

            // column
            helper.getUnifiedColumnRepository().save(resource);
            // document
            helper.getUnifiedDocumentRepository().save(resource);

            transaction.commit();

            LOG.debug("Resource Saved: " + resource);

            //Publish the event
            helper.getEventBus().post(Event.from(ResourceUtils.map(resource, Resource.class)), RoutingKey.of(resource.getResourceType(), Resource.State.UPDATED));
        }catch (Exception e){
            throw new RuntimeException("Unexpected error while saving resource: "+resource.getUri(),e);
        }
    }

}
