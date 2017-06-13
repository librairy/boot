/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.eventbus.guava;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by cbadenes on 26/11/15.
 */
public class GuavaCondition implements Condition {

    private static final Logger LOG = LoggerFactory.getLogger(GuavaCondition.class);

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String envVar = System.getenv("LIBRAIRY_EVENTBUS_HOST");
        boolean condition = (!Strings.isNullOrEmpty(envVar) && "local".equalsIgnoreCase(envVar))
                || (Strings.isNullOrEmpty(envVar) && "local".equalsIgnoreCase(conditionContext.getEnvironment().getProperty("librairy.eventbus.host")));
        return condition;
    }
}
