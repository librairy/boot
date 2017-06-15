/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.system.column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.cassandra.core.keyspace.KeyspaceOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Arrays;
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

//        schemaScripts.add("create keyspace if not exists research with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};");
//
//
//        schemaScripts.add("create table if not exists research.CAMEL_IDEMPOTENT (NAME varchar, KEY varchar, primary key (NAME, KEY)) WITH compaction = {'class':'LeveledCompactionStrategy'} AND gc_grace_seconds = 86400;");
//        schemaScripts.add("create index if not exists on research.CAMEL_IDEMPOTENT (NAME);");
//        schemaScripts.add("create index if not exists on research.CAMEL_IDEMPOTENT (KEY);");

        schemaScripts.add("create table if not exists research.sources(uri text, creationTime text, name text, description text, protocol text, url text, domain text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.sources (creationTime);");
        schemaScripts.add("create index if not exists on research.sources (name);");
        schemaScripts.add("create index if not exists on research.sources (protocol);");
        schemaScripts.add("create index if not exists on research.sources (url);");
        schemaScripts.add("create index if not exists on research.sources (domain);");

        schemaScripts.add("create table if not exists research.counts(num counter, name varchar, primary key(name));");

        schemaScripts.add("create table if not exists research.contains(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.contains (startUri);");
        schemaScripts.add("create index if not exists on research.contains (endUri);");

        schemaScripts.add("create table if not exists research.aggregates(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.aggregates (startUri);");
        schemaScripts.add("create index if not exists on research.aggregates (endUri);");

        schemaScripts.add("create table if not exists research.provides(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.provides (startUri);");
        schemaScripts.add("create index if not exists on research.provides (endUri);");

        schemaScripts.add("create table if not exists research.composes(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.composes (startUri);");
        schemaScripts.add("create index if not exists on research.composes (endUri);");

        schemaScripts.add("create table if not exists research.bundles(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.bundles (startUri);");
        schemaScripts.add("create index if not exists on research.bundles (endUri);");

        schemaScripts.add("create table if not exists research.similarTo(id bigint, uri text, creationTime text, domain text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.similarTo (startUri);");
        schemaScripts.add("create index if not exists on research.similarTo (endUri);");
        schemaScripts.add("create index if not exists on research.similarTo (domain);");

        schemaScripts.add("create table if not exists research.dealsWith(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.dealsWith (startUri);");
        schemaScripts.add("create index if not exists on research.dealsWith (endUri);");

        schemaScripts.add("create table if not exists research.emergesIn(id bigint, uri text, creationTime text, analysis text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.emergesIn (startUri);");
        schemaScripts.add("create index if not exists on research.emergesIn (endUri);");
        schemaScripts.add("create index if not exists on research.emergesIn (analysis);");

        schemaScripts.add("create table if not exists research.embeddedIn(id bigint, uri text, creationTime text, vector list<double>, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.embeddedIn (startUri);");
        schemaScripts.add("create index if not exists on research.embeddedIn (endUri);");

        schemaScripts.add("create table if not exists research.mentions(id bigint, uri text, creationTime text, times bigint, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.mentions (startUri);");
        schemaScripts.add("create index if not exists on research.mentions (endUri);");

        schemaScripts.add("create table if not exists research.describes(id bigint, uri text, creationTime text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.describes (startUri);");
        schemaScripts.add("create index if not exists on research.describes (endUri);");

        schemaScripts.add("create table if not exists research.pairsWith(id bigint, uri text, creationTime text, domain text, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.pairsWith (startUri);");
        schemaScripts.add("create index if not exists on research.pairsWith (endUri);");
        schemaScripts.add("create index if not exists on research.pairsWith (domain);");

        schemaScripts.add("create table if not exists research.hypernymOf(id bigint, uri text, creationTime text, domain text, provenances map<text,double>, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.hypernymOf (startUri);");
        schemaScripts.add("create index if not exists on research.hypernymOf (endUri);");
        schemaScripts.add("create index if not exists on research.hypernymOf (domain);");

        schemaScripts.add("create table if not exists research.appearedIn(id bigint, uri text, creationTime text, times bigint, subtermOf bigint, supertermOf bigint, cvalue double, consensus double, pertinence double, probability double, termhood double, startUri text, endUri text, weight double, primary key (uri));");
        schemaScripts.add("create index if not exists on research.appearedIn (startUri);");
        schemaScripts.add("create index if not exists on research.appearedIn (endUri);");

        schemaScripts.add("create table if not exists research.serializations(id bigint, uri text, creationTime text, instance blob, primary key (uri));");
        schemaScripts.add("create index if not exists on research.serializations (creationTime);");

        schemaScripts.add("create table if not exists research.domains(uri text, creationTime text, name text, description text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.domains (creationTime);");
        schemaScripts.add("create index if not exists on research.domains (name);");

        schemaScripts.add("create table if not exists research.documents(uri text, creationTime text, publishedOn text, publishedBy text, authoredOn text, authoredBy text, contributedBy text, retrievedBy text, retrievedFrom text, retrievedOn text, format text, language text, title text, subject text, description text, rights text, type text, content text, tokens text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.documents (creationTime);");
        schemaScripts.add("create index if not exists on research.documents (publishedOn);");
        schemaScripts.add("create index if not exists on research.documents (publishedBy);");
        schemaScripts.add("create index if not exists on research.documents (authoredOn);");
        schemaScripts.add("create index if not exists on research.documents (authoredBy);");
        schemaScripts.add("create index if not exists on research.documents (contributedBy);");
        schemaScripts.add("create index if not exists on research.documents (retrievedBy);");
        schemaScripts.add("create index if not exists on research.documents (retrievedFrom);");
        schemaScripts.add("create index if not exists on research.documents (retrievedOn);");
        schemaScripts.add("create index if not exists on research.documents (format);");
        schemaScripts.add("create index if not exists on research.documents (language);");
        schemaScripts.add("create index if not exists on research.documents (title);");
        schemaScripts.add("create index if not exists on research.documents (subject);");
        schemaScripts.add("create index if not exists on research.documents (rights);");
        schemaScripts.add("create index if not exists on research.documents (type);");

        schemaScripts.add("create table if not exists research.items(uri text, creationTime text, authoredOn text, authoredBy text, contributedBy text, format text, language text, title text, subject text, description text, url text, type text, content text, tokens text, annotated text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.items (creationTime);");
        schemaScripts.add("create index if not exists on research.items (authoredOn);");
        schemaScripts.add("create index if not exists on research.items (authoredBy);");
        schemaScripts.add("create index if not exists on research.items (contributedBy);");
        schemaScripts.add("create index if not exists on research.items (format);");
        schemaScripts.add("create index if not exists on research.items (language);");
        schemaScripts.add("create index if not exists on research.items (title);");
        schemaScripts.add("create index if not exists on research.items (subject);");
        schemaScripts.add("create index if not exists on research.items (url);");
        schemaScripts.add("create index if not exists on research.items (type);");

        schemaScripts.add("create table if not exists research.parts(uri text, creationTime text, sense text, content text, tokens text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.parts (creationTime);");
        schemaScripts.add("create index if not exists on research.parts (sense);");

        schemaScripts.add("create table if not exists research.terms(uri text, creationTime text, content text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.terms (creationTime);");
        schemaScripts.add("create index if not exists on research.terms (content);");

        schemaScripts.add("create table if not exists research.words(uri text, creationTime text, content text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.words (creationTime);");
        schemaScripts.add("create index if not exists on research.words (content);");

        schemaScripts.add("create table if not exists research.topics(uri text, creationTime text, content text, analysis text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.topics (creationTime);");
        schemaScripts.add("create index if not exists on research.topics (analysis);");

        schemaScripts.add("create table if not exists research.analyses(uri text, creationTime text, type text, description text, configuration text, domain text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.analyses (creationTime);");
        schemaScripts.add("create index if not exists on research.analyses (type);");
        schemaScripts.add("create index if not exists on research.analyses (configuration);");
        schemaScripts.add("create index if not exists on research.analyses (domain);");

        schemaScripts.add("create table if not exists research.filters(uri text, creationTime text, content text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.filters (creationTime);");
        schemaScripts.add("create index if not exists on research.filters (content);");

        schemaScripts.add("create table if not exists research.paths(uri text, creationTime text, start text, end text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.paths (creationTime);");
        schemaScripts.add("create index if not exists on research.paths (start);");
        schemaScripts.add("create index if not exists on research.paths (end);");


        schemaScripts.add("create table if not exists research.listeners(uri text, creationTime text, route text, primary key (uri));");
        schemaScripts.add("create index if not exists on research.listeners (creationTime);");
        schemaScripts.add("create index if not exists on research.listeners (route);");

        schemaScripts.add("create table if not exists research.annotations(uri text, resource text, type text, creationTime text, creator text, format text, language text, value map<text,text>, description text, purpose text, score double, selection map<text,text>, primary key(uri));");
        schemaScripts.add("create index if not exists on research.annotations (resource);");
        schemaScripts.add("create index if not exists on research.annotations (type);");
        schemaScripts.add("create index if not exists on research.annotations (creationTime);");
        schemaScripts.add("create index if not exists on research.annotations (creator);");
        schemaScripts.add("create index if not exists on research.annotations (purpose);");

        return schemaScripts;
    }

}
