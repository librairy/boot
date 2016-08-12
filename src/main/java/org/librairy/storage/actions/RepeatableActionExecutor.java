package org.librairy.storage.actions;

import org.librairy.storage.exception.RepositoryNotFound;
import org.neo4j.ogm.exception.CypherException;
import org.neo4j.ogm.exception.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by cbadenes on 12/02/16.
 */
public abstract class RepeatableActionExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(RepeatableActionExecutor.class);

    private static final Integer WAITING_TIME = 500; //msecs

    private static final Integer MAX_RETRIES = 5;


    protected void waitForRetry(Integer retries){
        try {

            Thread.sleep(Double.valueOf(Math.exp(retries)*WAITING_TIME).longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Bug on Neo4j-OGM https://github.com/neo4j/neo4j/issues/6178
     * @param retries
     * @param id
     * @param function
     * @return
     */
    protected Optional<Object> performRetries(Integer retries, String id, RepeatableAction function){
        try {
            return Optional.of(function.run());
        }catch (ResultProcessingException | NullPointerException e){
            if (( e.getCause() != null) &&
                    ( e.getCause() instanceof CypherException) &&
                    (e.getCause().getMessage().contains("ConstraintViolation"))){
                LOG.warn("Constraint Violation trying to execute: " + id + "=> " + ((CypherException) e.getCause()).getDescription());
                return Optional.empty();
            }
            else if (retries > MAX_RETRIES){
                LOG.error("Error executing "+id+" after " + MAX_RETRIES + " retries",e);
                // TODO Remove retries
                return Optional.empty();
            }
            else{
                LOG.warn("Trying to retry "+id+": " + retries);
                waitForRetry(retries);
                // TODO Remove retries
                return performRetries(++retries,id,function);
            }
        }catch (RepositoryNotFound e){
            LOG.warn(e.getMessage());
            return Optional.empty();
        }catch (Exception e){
            LOG.error("Error on operation: " + id, e);
            return Optional.empty();
        }
    }

//    protected Optional<Object> performRetries(Integer retries, String id, RepeatableAction function){
//        try {
//            return Optional.of(function.run());
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
