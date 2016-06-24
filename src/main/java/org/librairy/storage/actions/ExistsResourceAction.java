package org.librairy.storage.actions;

import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class ExistsResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(ExistsResourceAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public ExistsResourceAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Save a resource
     */
    public boolean withUri(String uri){
        try{
            return helper.getUnifiedDocumentRepository().exists(type,uri);
        }catch (Exception e){
            LOG.error("Unexpected error while checking resource: "+uri,e);
        }
        return false;
    }

}
