package org.librairy.storage.system.column.templates;

import com.datastax.driver.core.querybuilder.Select;
import org.librairy.model.domain.LinkableElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 14/04/16:
 *
 * @author cbadenes
 */
@Component
public class ColumnTemplate {

    @Autowired
    private CassandraOperations cassandraTemplate;


    public List<LinkableElement> query(Select select){
        return cassandraTemplate.queryForList(select, LinkableElement.class);
    }

}
