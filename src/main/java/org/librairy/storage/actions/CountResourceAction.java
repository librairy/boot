package org.librairy.storage.actions;

import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.Helper;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class CountResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(CountResourceAction.class);

    private final Helper helper;
    private final Resource.Type type;

    public CountResourceAction(Helper helper, Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Count all relations
     */
    public long all(){
        long size = 0;
        try{

            if (helper.getTemplateFactory().handle(type)){
                size = helper.getTemplateFactory().of(type).countAll();
            }else{
                size = helper.getUnifiedNodeGraphRepository().count(type);
            }

            LOG.debug("Count All: "+type.name());
        }catch (Exception e){
            LOG.error("Unexpected error during counting all '"+type,e);
        }
        return size;
    }

}
