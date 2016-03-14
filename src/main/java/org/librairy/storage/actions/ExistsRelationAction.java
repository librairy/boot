package org.librairy.storage.actions;

import org.librairy.storage.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class ExistsRelationAction {

    private static final Logger LOG = LoggerFactory.getLogger(ExistsRelationAction.class);

    private final Helper helper;
    private final org.librairy.model.domain.relations.Relation.Type type;

    public ExistsRelationAction(Helper helper, org.librairy.model.domain.relations.Relation.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Save a resource
     */
    public boolean withUri(String uri){
        try{
            return helper.getUnifiedEdgeGraphRepository().exists(type,uri);
        }catch (Exception e){
            LOG.error("Unexpected error while checking resource: "+uri,e);
        }
        return false;
    }

}
