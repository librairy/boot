/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.session;

import org.librairy.storage.actions.RepeatableActionExecutor;
import org.neo4j.ogm.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 03/02/16.
 */
public class UnifiedTransaction extends RepeatableActionExecutor{

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedTransaction.class);

    private Transaction neo4jTransaction;

    public void commit(){

        if (neo4jTransaction != null) {
            performRetries(0,"Commit transaction: " + neo4jTransaction, () -> {
                neo4jTransaction.commit();
                return 1;
            });
        }
    }

    public void setNeo4jTransaction(Transaction neo4jTransaction) {
        this.neo4jTransaction = neo4jTransaction;
    }
}
