/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.actions;

import com.google.common.base.Strings;
import org.librairy.boot.model.Event;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.model.modules.RoutingKey;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.model.utils.TimeUtils;
import org.librairy.boot.storage.session.UnifiedTransaction;
import org.librairy.boot.storage.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 04/02/16.
 */
public class SaveResourceAction {

    private static final Logger LOG = LoggerFactory.getLogger(SaveResourceAction.class);


    public SaveResourceAction(Helper helper, Resource resource){

        // initialize URI
        if (!resource.hasUri()){
            String uri;
            switch(resource.getResourceType()){
                case FILTER:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asFilter()
                            .getContent());
                    break;
                case PATH:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asPath()
                            .getStart()+resource.asPath().getEnd());
                    break;
                case WORD:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asWord()
                            .getContent());
                    break;
                case TERM:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asTerm()
                            .getContent());
                    break;
                case TOPIC:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asTopic()
                            .getContent());
                    break;
                case DOCUMENT:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),
                            (Strings.isNullOrEmpty(resource.asDocument().getTitle()))? TimeUtils.asISO() : resource
                                    .asDocument().getTitle());
                    break;
                case ITEM:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asItem()
                            .getContent());
                    break;
                case PART:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),resource.asPart()
                            .getContent());
                    break;
                case DOMAIN:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),
                            (Strings.isNullOrEmpty(resource.asDomain().getName()))? TimeUtils.asISO() : resource.asDomain().getName());
                    helper.getKeyspaceDao().createKeyspace(uri);
                    helper.getCounterDao().initialize(uri);
                    helper.getParametersDao().initialize(uri);
                    helper.getItemsDao().initialize(uri);
                    helper.getPartsDao().initialize(uri);
                    helper.getSubdomainsDao().initialize(uri);
                    break;
                case SOURCE:
                    uri = helper.getUriGenerator().basedOnContent(resource.getResourceType(),
                            (Strings.isNullOrEmpty(resource.asSource().getName()))? TimeUtils.asISO() : resource.asSource()
                                    .getName());
                    break;
                default:
                    uri = helper.getUriGenerator().newFor(resource.getResourceType());
            }

            resource.setUri(uri);

            // increment counter
            helper.getCounterDao().increment(resource.getResourceType().route());
        }


        try{
            helper.getSession().clean();
            UnifiedTransaction transaction = helper.getSession().beginTransaction();

            LOG.debug("trying to save: " + resource);

            // graph
            // TODO remove it
//            if (helper.getTemplateFactory().handle(resource.getResourceType())){
//                helper.getTemplateFactory().of(resource.getResourceType()).save(resource);
//            }else{
//                helper.getUnifiedNodeGraphRepository().save(resource);
//            }

            // column
            helper.getUnifiedColumnRepository().save(resource);
            // document
            helper.getUnifiedDocumentRepository().save(resource);

            transaction.commit();

            LOG.debug("Resource Saved: " + resource);

            //Publish the event
            helper.getEventBus().post(Event.from(ResourceUtils.map(resource, Resource.class)), RoutingKey.of(resource.getResourceType(), Resource.State.CREATED));
        }catch (Exception e){
            throw new RuntimeException("Unexpected error while saving resource: "+resource.getUri(),e);
        }
    }

}
