package org.librairy.storage;

import com.datastax.driver.core.querybuilder.Select;
import com.google.common.collect.ImmutableMap;
import org.librairy.model.domain.LinkableElement;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.actions.*;
import org.librairy.storage.system.column.templates.ColumnTemplate;
import org.librairy.storage.system.document.templates.DocumentTemplate;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

    private static final Logger LOG = LoggerFactory.getLogger(UDM.class);

    @Autowired
    Helper helper;


    @Autowired
    TemplateExecutor neo4jExecutor;

    @Autowired
    ColumnTemplate columnTemplate;

    @Autowired
    DocumentTemplate documentTemplate;

    public Iterator<Map<String, Object>> queryGraph(String query){
        Optional<Result> result = neo4jExecutor.query(query, ImmutableMap.of());
        if (!result.isPresent()) return Collections.EMPTY_LIST.iterator();
        return result.get().queryResults().iterator();
    }


    public List<LinkableElement> queryColumn(Select select){
        return columnTemplate.query(select);
    }


    public List<String> queryDocument(SearchQuery select){
        return documentTemplate.query(select);
    }

    /**
     * Save a resource
     * @param resource
     */
    public SaveResourceAction save(org.librairy.model.domain.resources.Resource resource){return new SaveResourceAction(helper,resource);
    }


    /**
     * Save a relation
     * @param relation
     */
    public SaveRelationAction save(Relation relation){
        return new SaveRelationAction(helper,relation);
    }

    /**
     * Check if a 'type' resource identified by 'uri' exists
     * @param type
     * @return boolean
     */
    public ExistsResourceAction exists(org.librairy.model.domain.resources.Resource.Type type){
        return new ExistsResourceAction(helper,type);
    }

    /**
     * Check if a 'type' relation identified by 'uri' exists
     * @param type
     * @return boolean
     */
    public ExistsRelationAction exists(Relation.Type type){
        return new ExistsRelationAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return resource
     */
    public ReadResourceAction read(org.librairy.model.domain.resources.Resource.Type type){return new ReadResourceAction(helper,type);
    }

    /**
     * Read the 'type' resource identified by 'uri'
     * @param type
     * @return relation
     */
    public ReadRelationAction read(Relation.Type type){
        return new ReadRelationAction(helper,type);
    }


    /**
     * Search resources
     * @param type
     * @return uris
     */
    public SearchResourceAction find(org.librairy.model.domain.resources.Resource.Type type){
        return new SearchResourceAction(helper,type);
    }

    /**
     * Search relations
     * @param type
     * @return uris
     */
    public SearchRelationAction find(Relation.Type type){
        return new SearchRelationAction(helper,type);
    }

    /**
     * Count 'type' resources
     * @param type
     * @return uris
     */
    public CountResourceAction count(Resource.Type type){
        return new CountResourceAction(helper,type);
    }

    /**
     * Count 'type' relations
     * @param type
     * @return uris
     */
    public CountRelationAction count(Relation.Type type){
        return new CountRelationAction(helper,type);
    }

    /**
     * Delete 'type' resources
     * @param type
     * @return
     */
    public DeleteResourceAction delete(org.librairy.model.domain.resources.Resource.Type type){
        return new DeleteResourceAction(helper,type);
    }

    /**
     * Delete 'type' relations
     * @param type
     * @return
     */
    public DeleteRelationAction delete(Relation.Type type){
        return new DeleteRelationAction(helper,type);
    }

}
