/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.NumberFormat;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ResourcesInfo {

    private static final Logger LOG = LoggerFactory.getLogger(ResourcesInfo.class);

    final long mb = 1024 * 1024;

    public long getMaxMemory(){
        return Runtime.getRuntime().maxMemory()/mb;
    }

    public long getAllocatedMemory(){
        return Runtime.getRuntime().totalMemory()/mb;
    }

    public long getFreeMemory(){
        return Runtime.getRuntime().freeMemory()/mb;
    }

    public long getTotalFreeMemory(){
        return getFreeMemory() + (getMaxMemory() - getAllocatedMemory());
    }

    public long getAvailableProcessors(){
        return Runtime.getRuntime().availableProcessors();
    }

    @PostConstruct
    public void setup(){
        final NumberFormat format = NumberFormat.getInstance();
        final String mega = " MB";
        LOG.info("========================== Resources Info ==========================");
        LOG.info("Available Processors: " + format.format(getAvailableProcessors()));
        LOG.info("Free memory: " + format.format(getFreeMemory()) + mega);
        LOG.info("Allocated memory: " + format.format(getAllocatedMemory()) + mega);
        LOG.info("Max memory: " + format.format(getMaxMemory()) + mega);
        LOG.info("Total free memory: " + format.format(getTotalFreeMemory()) + mega);
        LOG.info("=================================================================\n");
    }
}
