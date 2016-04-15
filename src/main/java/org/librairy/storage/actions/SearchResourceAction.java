package org.librairy.storage.actions;

import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.Helper;
import org.librairy.storage.session.UnifiedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SearchResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResourceAction.class);

    private final Helper helper;
    private final org.librairy.model.domain.resources.Resource.Type type;

    public SearchResourceAction(Helper helper, org.librairy.model.domain.resources.Resource.Type type){
        this.helper = helper;
        this.type = type;
    }

    /**
     * Find all resources
     */
    public List<String> all(){
        LOG.debug("Finding " + type.name() + "s");
        List<String> uris = new ArrayList<>();
        try{


            if (helper.getTemplateFactory().handle(type)){
                helper.getTemplateFactory().of(type).findAll().forEach(x -> uris.add(x.getUri()));
            }else{
                helper.getUnifiedNodeGraphRepository().findAll(type).forEach(x -> uris.add(x.getUri()));
            }

            LOG.trace(type.name() + "s: " + uris);
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return uris;
    }

    /**
     * Find resources attached to other resource (directly or indirectly)
     * @param referenceType
     * @param referenceURI
     * @return
     */
    public List<String> from(Resource.Type referenceType, String referenceURI){
        LOG.debug("Finding " + type.name() + "s in " + referenceType + ": " + referenceURI);
        List<String> uris = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();


            if (helper.getTemplateFactory().handle(type)){
                helper.getTemplateFactory().of(type).findFrom(referenceType,referenceURI).forEach(x -> uris.add(x.getUri()));
            }else{
                helper.getUnifiedNodeGraphRepository().findFrom(type, referenceType,referenceURI).forEach(x -> uris.add(x.getUri()));
            }

            transaction.commit();
            LOG.debug("In "+referenceType+": " + referenceURI + " found: ["+type + "]: " + uris);
        }catch (Exception e){
            LOG.error("Unexpected error while finding " + type +"s in " + referenceType + ": " + referenceURI,e);
        }
        return uris;
    }

    /**
     * Find resources by a field value
     * @param field
     * @param value
     * @return
     */
    public List<String> by(String field, String value){
        LOG.debug("Finding " + type.name() + "s");
        List<String> uris = new ArrayList<>();
        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            helper.getUnifiedColumnRepository().findBy(type, field,value).forEach(x -> uris.add(x.getUri()));

            transaction.commit();
            LOG.debug("By "+field+": '" + value+ "' found: ["+type + "]: " + uris);
        }catch (Exception e){
            LOG.error("Unexpected error while getting all " + type,e);
        }
        return uris;
    }

}
