package org.librairy.storage.system.document.templates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 14/04/16:
 *
 * @author cbadenes
 */
@Component
public class DocumentTemplate {

    @Autowired
    ElasticsearchTemplate template;

    public List<String> query(SearchQuery query){
        return template.queryForIds(query);
    }

}
