/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.actions;

import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.storage.Helper;
import org.librairy.boot.storage.exception.RepositoryNotFound;
import org.librairy.boot.storage.generator.URIGenerator;
import org.librairy.boot.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

            // delete all counters
            helper.getCounterDao().truncate();
//            helper.getCounterDao().initialize();

            List<Relation.Type> types = (type.equals(Relation.Type.ANY)) ? Arrays.asList
                    (Relation.Type.values()) : Arrays.asList(new Relation.Type[]{type});

            LOG.info("Ready to delete the following relation types: " + types);

            types.stream().filter(x -> !x.equals(Relation.Type.ANY)).forEach(t ->{
                try{

                    // TODO remove it
//                    if (helper.getTemplateFactory().handle(t)){
//                        helper.getTemplateFactory().of(t).deleteAll();
//                    }else{
//                        helper.getUnifiedEdgeGraphRepository().deleteAll(t);
//                    }

                    // Column Database
                    helper.getUnifiedColumnRepository().deleteAll(t);

                    LOG.debug("Deleted All: "+t.name());

                }catch (RepositoryNotFound e){
                    LOG.warn("" + e.getMessage());
                }
            });

            transaction.commit();


            //Publish the event
            // TODO
        }catch (Exception e){
            throw new RuntimeException("Unexpected error during delete all '"+type,e);
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

            // Column Database
            helper.getUnifiedColumnRepository().delete(type,uri);

            transaction.commit();

            LOG.debug("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            helper.getEventBus().post(Event.from(uri), RoutingKey.of(type, Relation.State.DELETED));
        }catch (Exception e){
            throw new RuntimeException("Unexpected error during delete of '"+uri,e);
        }
    }


}
