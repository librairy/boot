package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.modules.RoutingKey;
import org.librairy.storage.Helper;
import org.librairy.storage.exception.RepositoryNotFound;
import org.librairy.storage.session.UnifiedTransaction;
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
    private final org.librairy.model.domain.resources.Resource.Type type;

    public DeleteResourceAction(Helper helper, org.librairy.model.domain.resources.Resource.Type type){
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

            List<org.librairy.model.domain.resources.Resource.Type> types = (type.equals(org.librairy.model.domain.resources.Resource.Type.ANY)) ? Arrays.asList(org.librairy.model.domain.resources.Resource.Type.values()) : Arrays.asList(new org.librairy.model.domain.resources.Resource.Type[]{type});

            LOG.info("Ready to delete the following resource types: " + types);

            types.stream().filter(x -> !x.equals(org.librairy.model.domain.resources.Resource.Type.ANY)).forEach(t ->{
                try{
                    helper.getUnifiedColumnRepository().deleteAll(t);
                    helper.getUnifiedDocumentRepository().deleteAll(t);


                    if (helper.getTemplateFactory().handle(t)){
                        helper.getTemplateFactory().of(t).deleteAll();
                    }else{
                        helper.getUnifiedNodeGraphRepository().deleteAll(t);
                    }

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

            helper.getUnifiedColumnRepository().delete(type,uri);
            helper.getUnifiedDocumentRepository().delete(type,uri);

            if (helper.getTemplateFactory().handle(type)){
                helper.getTemplateFactory().of(type).deleteOne(uri);
            }else{
                helper.getUnifiedNodeGraphRepository().delete(type,uri);
            }

            transaction.commit();

            LOG.debug("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            helper.getEventBus().post(Event.from(uri), RoutingKey.of(type, Resource.State.DELETED));

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

}
