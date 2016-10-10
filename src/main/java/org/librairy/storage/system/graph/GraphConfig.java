/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.graph;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.conversion.MetaDataDrivenConversionService;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration("org.librairy.storage.system.graph")
@ComponentScan({"org.librairy.storage.system.graph.repository"})
@EnableNeo4jRepositories(basePackages = {"org.librairy.storage.system.graph.repository"})
@EnableTransactionManagement
public class GraphConfig extends Neo4jConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(GraphConfig.class);

    @Value("#{environment['LIBRAIRY_GRAPHDB_HOST']?:'${librairy.graphdb.host}'}")
    String hosts;

    @Value("#{environment['LIBRAIRY_GRAPHDB_PORT']?:${librairy.graphdb.port}}")
    Integer port;


    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        LOG.info("Initializying Neo4j connection to: " + hosts + " " + port + " ..") ;
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                .setURI("http://"+hosts+":"+port);
        LOG.info("Initialized Neo4j connection to: " + hosts + " " + port);
        return config;
    }

    @Override
    @Bean
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        return new SessionFactory(getConfiguration(),"org.librairy.storage.system.graph.domain");
    }

    @Override
    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        Session session = super.getSession();
        return session;
    }


//    @Bean
//    public ConversionService springConversionService() {
//        return new MetaDataDrivenConversionService(getSessionFactory().metaData());
//    }


    @Bean
    public ConversionService conversionService() {
        ConversionService conversionService = new MetaDataDrivenConversionService(getSessionFactory().metaData());
        DefaultConversionService.addDefaultConverters((GenericConversionService) conversionService);
        return conversionService;
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
