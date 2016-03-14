package org.librairy.storage.exception;

/**
 * Created by cbadenes on 12/02/16.
 */
public class RepositoryNotFound extends IllegalArgumentException {

    public RepositoryNotFound(String msg){
        super(msg);
    }
}
