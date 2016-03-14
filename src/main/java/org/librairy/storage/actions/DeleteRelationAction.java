package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.Helper;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.StreamSupport;

/**
 * Created by cbadenes on 04/02/16.
 */
public class DeleteRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteRelationAction.class);

    private final Helper helper;
    private final Relation.Type type;

    public DeleteRelationAction(Helper helper, Relation.Type type){
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

            helper.getUnifiedEdgeGraphRepository().deleteAll(type);

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

            helper.getUnifiedEdgeGraphRepository().delete(type,uri);

            transaction.commit();

            LOG.debug("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            //TODO

        }catch (Exception e){
            LOG.error("Unexpected error during delete of '"+uri,e);
        }
    }

    public void in(Resource.Type refType, String uri){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            if (helper.getTemplateFactory().handle(type)){
                helper.getTemplateFactory().of(type).deleteIn(refType,uri);
            }else{
                Iterable<Relation> pairs = helper.getUnifiedEdgeGraphRepository().findFrom(type,refType, uri);
                if (pairs != null){
                    // TODO Check this
                    StreamSupport.stream(pairs.spliterator(), false).parallel().forEach(pair -> {
                        helper.getUnifiedEdgeGraphRepository().delete(type,pair.getUri());
                        helper.getEventBus().post(Event.from(pair.getUri()), org.librairy.model.modules.RoutingKey.of(type, Relation.State.DELETED));
                        LOG.debug("Deleted: "+type.name()+"[" + uri+"]");
                    });
    //                for (Relation pair : pairs) {
    //                    helper.getUnifiedEdgeGraphRepository().delete(type,pair.getUri());
    //                    helper.getEventBus().post(Event.from(pair.getUri()), RoutingKey.of(type, Relation.State.DELETED));
    //                }
                }
            }


            transaction.commit();

            LOG.debug("Deleted: "+type.name()+" in " + refType + "[" + uri+"]");
        }catch (Exception e){
            LOG.error("Unexpected error during delete of relations '"+ type + " in " + refType +" by uri "+uri,e);
        }
    }

}
