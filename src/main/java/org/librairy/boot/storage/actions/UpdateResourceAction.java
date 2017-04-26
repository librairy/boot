/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.actions;

import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.storage.Helper;
import org.librairy.boot.storage.session.UnifiedTransaction;
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

            transaction.commit();

            LOG.debug("Resource Saved: " + resource);

            //Publish the event
            helper.getEventBus().post(Event.from(ResourceUtils.map(resource, Resource.class)), RoutingKey.of(resource.getResourceType(), Resource.State.UPDATED));
        }catch (Exception e){
            throw new RuntimeException("Unexpected error while saving resource: "+resource.getUri(),e);
        }
    }

}
