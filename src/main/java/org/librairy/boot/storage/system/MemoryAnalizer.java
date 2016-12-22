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
public class MemoryAnalizer {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryAnalizer.class);

    @PostConstruct
    public void setup(){
        Runtime runtime = Runtime.getRuntime();
        final NumberFormat format = NumberFormat.getInstance();
        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024;
        final String mega = " MB";
        LOG.info("========================== Memory Info ==========================");
        LOG.info("Free memory: " + format.format(freeMemory / mb) + mega);
        LOG.info("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
        LOG.info("Max memory: " + format.format(maxMemory / mb) + mega);
        LOG.info("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        LOG.info("=================================================================\n");
    }
}
