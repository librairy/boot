/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph.template;

import org.apache.http.client.HttpResponseException;
import org.librairy.storage.actions.ExecutionResult;
import org.librairy.storage.actions.RepeatableActionExecutor;
import org.librairy.storage.exception.ConstraintException;
import org.neo4j.ogm.exception.CypherException;
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
import java.util.Arrays;
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
        Optional<ExecutionResult> result = performRetries(0, query + " => PARAMS: " + Arrays.toString(parameters.entrySet().toArray
                ()), () -> {
            Result res = template.query(query, parameters);
            return res;
        });
        return (result.isPresent())? Optional.of((Result)result.get().getResult()) : Optional.empty();
    }

    public Optional<Iterable> queryForObjects(Class objectType, String query, Map<String, ?> parameters){
        Optional<ExecutionResult> result = performRetries(0, query + " => PARAMS: " + Arrays.toString(parameters.entrySet().toArray
                ()), () -> {
            Iterable res = template.queryForObjects(objectType, query, parameters);
            return res;
        });
        return (result.isPresent())? Optional.of((Iterable)result.get().getResult()) : Optional.empty();
    }


    public Optional<Integer> execute(String query, Map<String, Object> parameters){
        Optional<ExecutionResult> result = performRetries(0, query + " => PARAMS: " + Arrays.toString(parameters.entrySet().toArray
                ()),
                () -> {
            template.clear();
            try{
                QueryStatistics res = template.execute(query, parameters);
                // TODO This part of code should be removed when Neo4j uses Bolt
                if (!res.containsUpdates() && (!query.contains("delete"))) {
                    throw new ResultProcessingException("No contains updates", new HttpResponseException(404,"Not found"));
                }
                return res;
            }catch (CypherException e){
                if (e.getMessage().contains("Neo.ClientError.Schema.ConstraintViolation")){
                    throw new ConstraintException("Already exists node by query: " +query+" -> " + parameters);
                }
                throw e;
            }
        });
        return (result.isPresent())? Optional.of(result.get().getRetries()) : Optional.empty();
    }


    public void deleteAll(Class<Object> type){
        template.deleteAll(type);
    }


}
