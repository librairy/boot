/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by cbadenes on 15/03/16.
 */
@Configuration("librairy.boot.dao.test")
@ComponentScan({"org.librairy.boot.eventbus", "org.librairy.boot.storage.dao","org.librairy.boot.storage.system.column" })
@PropertySource({"classpath:boot.properties"})
public class DaoTest {
}
