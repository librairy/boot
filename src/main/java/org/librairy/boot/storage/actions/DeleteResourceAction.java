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
import org.librairy.boot.storage.exception.RepositoryNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.librairy.boot.storage.session.UnifiedTransaction;
import org.librairy.boot.storage.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cbadenes on 04/02/16.
 */
public class DeleteResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteResourceAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public DeleteResourceAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Delete all resources
     */
    public void all(){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // delete all counters
            helper.getCounterDao().remove();
            helper.getCounterDao().initialize();

            List<Resource.Type> types = (type.equals(Resource.Type.ANY)) ? Arrays.asList(Resource.Type.values()) : Arrays.asList(new Resource.Type[]{type});

            LOG.info("Ready to delete the following resource types: " + types);

            types.stream().filter(x -> !x.equals(Resource.Type.ANY)).forEach(t ->{
                try{
                    helper.getUnifiedColumnRepository().deleteAll(t);

                }catch (RepositoryNotFound e){
                    LOG.warn("" + e.getMessage());
                }
            });


            transaction.commit();

            LOG.debug("Deleted All: "+type.name());

            //Publish the event
            // TODO
        }catch (Exception e){
            LOG.error("Unexpected error during delete all '"+type,e);
        }
    }

    /**
     * Delete resource identified by 'uri'
     * @param uri
     */
    public void byUri(String uri){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            // decrement global counter
            helper.getCounterDao().decrement(type.route());

            switch (type){
                case DOMAIN:
                    try{
                        helper.getKeyspaceDao().removeKeyspace(uri);
                    }catch (Exception e){
                        LOG.warn("Unexpected error trying to delete keyspace from '" + uri + "'",e);
                    }
                    break;
            }



            helper.getUnifiedColumnRepository().delete(type,uri);

            transaction.commit();

            LOG.debug("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            Resource resource = new Resource();
            resource.setUri(uri);
            helper.getEventBus().post(Event.from(ResourceUtils.map(resource, Resource.class)), RoutingKey.of(type, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

}
