package org.librairy.storage.system.graph.template;

import org.apache.http.client.HttpResponseException;
import org.librairy.storage.actions.RepeatableActionExecutor;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.neo4j.ogm.exception.ResultProcessingException;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class TemplateExecutor extends RepeatableActionExecutor{

    private static final Logger LOG = LoggerFactory.getLogger(TemplateExecutor.class);

    @Autowired
    Session session;

    Neo4jTemplate template;

    @PostConstruct
    public void setup(){
        this.template = new Neo4jTemplate(session);
    }


    public Optional<Result> query(String query, Map<String, ?> parameters){
        Long start = System.currentTimeMillis();
        Optional<Object> result = performRetries(0, query, () -> {
            Result res = template.query(query, parameters);
            return res;
        });
        Period period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.debug("Query : " + query + " in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds [" + query+"]");
        return (result.isPresent())? Optional.of((Result)result.get()) : Optional.empty();
    }


    public QueryStatistics execute(String query, Map<String, Object> parameters){
        Long start = System.currentTimeMillis();
        Optional<Object> result = performRetries(0, query, () -> {
            template.clear();
            QueryStatistics res = template.execute(query, parameters);
            // TODO This part of code should be removed when Neo4j uses Bolt
            if (!res.containsUpdates() && (!query.contains("delete"))) {
                throw new ResultProcessingException("No contains updates", new HttpResponseException(404,"Not found"));
            }
            return res;
        });
        Period period = new Interval(start, System.currentTimeMillis()).toPeriod();
        LOG.debug("Executed : " + query + " in: " + period.getMinutes() + " minutes, " + period.getSeconds() + " seconds [" + query+"]");
        if (!result.isPresent()) throw new RuntimeException("Operation not completed");
        return (QueryStatistics) result.get();
    }


    public void deleteAll(Class<Object> type){
        template.deleteAll(type);
    }


}
