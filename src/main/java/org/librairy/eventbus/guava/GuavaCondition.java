package org.librairy.eventbus.guava;

import com.google.common.base.Strings;
import org.librairy.Config;
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
        String sysHost  = System.getProperty(Config.EVENT_HOST);
        String envHost  = System.getenv(Config.EVENT_HOST);
        String host     = (Strings.isNullOrEmpty(envHost))? sysHost : envHost;

        boolean condition =  Strings.isNullOrEmpty(host) || host.startsWith("local");
        LOG.debug("Guava condition for: " + host + " is:" + condition);
        return condition;
    }
}
