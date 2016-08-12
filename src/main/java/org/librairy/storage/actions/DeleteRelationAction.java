package org.librairy.storage.actions;

import org.librairy.model.Event;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.Helper;
import org.librairy.storage.exception.RepositoryNotFound;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
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

            List<Relation.Type> types = (type.equals(Relation.Type.ANY)) ? Arrays.asList
                    (Relation.Type.values()) : Arrays.asList(new Relation.Type[]{type});

            LOG.info("Ready to delete the following relation types: " + types);

            types.stream().filter(x -> !x.equals(Relation.Type.ANY)).forEach(t ->{
                try{
                    if (helper.getTemplateFactory().handle(t)){
                        helper.getTemplateFactory().of(t).deleteAll();
                    }else{
                        helper.getUnifiedEdgeGraphRepository().deleteAll(t);
                    }

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

            if (helper.getTemplateFactory().handle(type)) {
                helper.getTemplateFactory().of(type).delete(uri);
            }else{
                helper.getUnifiedEdgeGraphRepository().delete(type,uri);
            }

            // Column Database
            helper.getUnifiedColumnRepository().delete(type,uri);

            transaction.commit();

            LOG.debug("Deleted: "+type.name()+"[" + uri+"]");

            //Publish the event
            //TODO

        }catch (Exception e){
            throw new RuntimeException("Unexpected error during delete of '"+uri,e);
        }
    }


    public void in(Resource.Type refType, String uri){
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            if (helper.getTemplateFactory().handle(type)){

                helper.getTemplateFactory().of(type).findIn(refType,uri).forEach(relation -> byUri(relation.getUri()));
                //helper.getTemplateFactory().of(type).deleteIn(refType,uri);
            }else{
                Iterable<Relation> pairs = helper.getUnifiedEdgeGraphRepository().findFrom(type,refType, uri);
                if (pairs != null){
                    StreamSupport.stream(pairs.spliterator(), false).parallel().forEach(pair -> {
                        helper.getUnifiedEdgeGraphRepository().delete(type,pair.getUri());

                        // Column Database
                        helper.getUnifiedColumnRepository().delete(type,pair.getUri());
                        LOG.debug("Deleted: "+type.name()+"[" + uri+"]");
                    });
                }
            }



            //Publish the event
            //TODO

            transaction.commit();

            LOG.debug("Deleted: "+type.name()+" in " + refType + "[" + uri+"]");
        }catch (Exception e){
            throw new RuntimeException("Unexpected error during delete of relations '"+ type + " in " + refType +" by uri "+uri,e);
        }
    }

}