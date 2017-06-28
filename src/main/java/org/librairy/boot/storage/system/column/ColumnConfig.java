/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column;

import org.apache.commons.lang.StringUtils;
import org.librairy.boot.utils.ResourceWaiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration("org.librairy.boot.storage.system.column")
@ComponentScan({"org.librairy.boot.storage.system.column.repository"})
@EnableCassandraRepositories(basePackages = {"org.librairy.boot.storage.system.column.repository"})
@EnableTransactionManagement
public class ColumnConfig extends AbstractCassandraConfiguration{

    private static final Logger LOG = LoggerFactory.getLogger(ColumnConfig.class);

    @Autowired
    private Environment env;

    @Value("#{environment['LIBRAIRY_COLUMNDB_HOST']?:'${librairy.columndb.host}'}")
    String hosts;

    @Value("#{environment['LIBRAIRY_COLUMNDB_PORT']?:${librairy.columndb.port}}")
    Integer port;

    @Bean
    @DependsOn("dbChecker")
    public CassandraClusterFactoryBean cluster(){
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        try{
            LOG.info("Initializying Cassandra connection to: " + hosts + " " + port + " ..");
            cluster.setContactPoints(hosts);
            cluster.setPort(port);
            List<CreateKeyspaceSpecification> specifications = new ArrayList<>();
            specifications.add(
                    new CreateKeyspaceSpecification(getKeyspaceName())
                    .ifNotExists()
                    .withSimpleReplication(1l)
            );
            cluster.setKeyspaceCreations(specifications);

//            SocketOptions options = new SocketOptions();
//            options.setReadTimeoutMillis(60000);
//            options.setConnectTimeoutMillis(20000);
//            cluster.setSocketOptions(options);
            LOG.info("Initialized Cassandra connection to: " + hosts + " " + port);
        }catch (Exception e){
            LOG.error("Error configuring cassandra connection parameters: ",e);
        }
        return cluster;
    }

    @Bean("dbChecker")
    public Boolean waitForService(){
        return ResourceWaiter.waitFor(StringUtils.substringBefore(hosts,","), port);
    }


//    @Bean(name = "cassandraMappingContext")
//    public CassandraMappingContext mappingContext() {
////        return new BasicCassandraMappingContext();
//        BasicCassandraMappingContext mappingContext =  new BasicCassandraMappingContext();
//        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), getKeyspaceName()));
//
//        return mappingContext;
//    }

//    @Bean(name = "cassandraConverter")
//    public CassandraConverter converter() {
//        return new MappingCassandraConverter(mappingContext());
//    }
//
//    @Bean
//    public CassandraSessionFactoryBean session() {
//
//        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
//        session.setCluster(cluster().getObject());
//        session.setKeyspaceName(getKeyspaceName());
//        session.setConverter(converter());
//        session.setSchemaAction(SchemaAction.RECREATE);
//
//        return session;
//    }

//    @Bean
//    public CassandraOperations cassandraTemplate() throws Exception {
//        return new CassandraTemplate(session().getObject());
//    }

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("librairy.columndb.keyspace");
    }


    @Override
    protected List<String> getStartupScripts() {

        LOG.info("Loading librairy schema ...");

        List<String> schemaScripts = new ArrayList<>();

        schemaScripts.add("create table if not exists librairy.contains(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.contains (startUri);");
        schemaScripts.add("create index if not exists on librairy.contains (endUri);");

        schemaScripts.add("create table if not exists librairy.similarTo(id bigint, uri text, creationTime text, domain text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.similarTo (startUri);");
        schemaScripts.add("create index if not exists on librairy.similarTo (endUri);");
        schemaScripts.add("create index if not exists on librairy.similarTo (domain);");

        schemaScripts.add("create table if not exists librairy.dealsWith(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.dealsWith (startUri);");
        schemaScripts.add("create index if not exists on librairy.dealsWith (endUri);");

        schemaScripts.add("create table if not exists librairy.emergesIn(id bigint, uri text, creationTime text, analysis text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.emergesIn (startUri);");
        schemaScripts.add("create index if not exists on librairy.emergesIn (endUri);");
        schemaScripts.add("create index if not exists on librairy.emergesIn (analysis);");

        schemaScripts.add("create table if not exists librairy.describes(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.describes (startUri);");
        schemaScripts.add("create index if not exists on librairy.describes (endUri);");

        schemaScripts.add("create table if not exists librairy.domains(uri text, creationTime text, name text, description text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.domains (creationTime);");
        schemaScripts.add("create index if not exists on librairy.domains (name);");

        schemaScripts.add("create table if not exists librairy.items(uri text, creationTime text, authoredOn text, authoredBy text, contributedBy text, format text, language text, title text, subject text, description text, url text, type text, content text, tokens text, annotated text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.items (creationTime);");
        schemaScripts.add("create index if not exists on librairy.items (authoredOn);");
        schemaScripts.add("create index if not exists on librairy.items (authoredBy);");
        schemaScripts.add("create index if not exists on librairy.items (contributedBy);");
        schemaScripts.add("create index if not exists on librairy.items (format);");
        schemaScripts.add("create index if not exists on librairy.items (language);");
        schemaScripts.add("create index if not exists on librairy.items (title);");
        schemaScripts.add("create index if not exists on librairy.items (subject);");
        schemaScripts.add("create index if not exists on librairy.items (url);");
        schemaScripts.add("create index if not exists on librairy.items (type);");

        schemaScripts.add("create table if not exists librairy.parts(uri text, creationTime text, sense text, content text, tokens text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.parts (creationTime);");
        schemaScripts.add("create index if not exists on librairy.parts (sense);");

        schemaScripts.add("create table if not exists librairy.topics(uri text, creationTime text, content text, analysis text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.topics (creationTime);");
        schemaScripts.add("create index if not exists on librairy.topics (analysis);");

        schemaScripts.add("create table if not exists librairy.filters(uri text, creationTime text, content text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.filters (creationTime);");
        schemaScripts.add("create index if not exists on librairy.filters (content);");

        schemaScripts.add("create table if not exists librairy.paths(uri text, creationTime text, start text, end text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.paths (creationTime);");
        schemaScripts.add("create index if not exists on librairy.paths (start);");
        schemaScripts.add("create index if not exists on librairy.paths (end);");

        schemaScripts.add("create table if not exists librairy.listeners(uri text, creationTime text, route text, primary key (uri));");
        schemaScripts.add("create index if not exists on librairy.listeners (creationTime);");
        schemaScripts.add("create index if not exists on librairy.listeners (route);");

        schemaScripts.add("create table if not exists librairy.annotations(uri text, resource text, type text, creationTime text, creator text, format text, language text, value map<text,text>, description text, purpose text, score double, selection map<text,text>, primary key(uri));");
        schemaScripts.add("create index if not exists on librairy.annotations (resource);");
        schemaScripts.add("create index if not exists on librairy.annotations (type);");
        schemaScripts.add("create index if not exists on librairy.annotations (creationTime);");
        schemaScripts.add("create index if not exists on librairy.annotations (creator);");
        schemaScripts.add("create index if not exists on librairy.annotations (purpose);");

        // code=2200 [Invalid query] message="Cannot include more than one non-primary key column 'typeFilter' in materialized view primary key"
        //schemaScripts.add("CREATE MATERIALIZED VIEW librairy.annotations_by_resource AS SELECT resource, typeFilter, creator, purposeFilter, uri FROM librairy.annotations WHERE resource IS NOT NULL AND uri IS NOT NULL PRIMARY KEY ((resource), typeFilter, creator, purposeFilter);");

        schemaScripts.add("create table if not exists librairy.annotations_by_resource(resource text, type text, creator text, purpose text, uri text, primary key((resource),type,purpose,creator));");

        schemaScripts.add("create table if not exists librairy.parameters_by_domain(domain text, parameter text, value text, primary key((domain),parameter));");
        schemaScripts.add("create table if not exists librairy.resources_by_domain(domain text, type text, resource text, name text, time text, tokens text, primary key((domain,type),resource));");
        schemaScripts.add("create table if not exists librairy.counters_by_domain(domain text, counter text, value counter, primary key((domain),counter));");

        return schemaScripts;
    }

}
