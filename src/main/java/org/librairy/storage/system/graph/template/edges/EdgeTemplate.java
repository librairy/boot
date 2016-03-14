package org.librairy.storage.system.graph.template.edges;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.librairy.model.domain.relations.Relation;
import org.librairy.storage.system.graph.repository.edges.UnifiedEdgeGraphRepositoryFactory;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by cbadenes on 28/02/16.
 */
public abstract class EdgeTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(EdgeTemplate.class);

    protected final Relation.Type type;

    @Autowired
    TemplateExecutor executor;

    @Autowired
    UnifiedEdgeGraphRepositoryFactory unifiedEdgeGraphRepositoryFactory;


    public EdgeTemplate(Relation.Type type){
        this.type = type;
    }


    public Relation.Type accept() {
        return type;
    }

    protected abstract String pathBy(org.librairy.model.domain.resources.Resource.Type type);

    protected abstract String pathBy(Relation.Type type);

    protected abstract TemplateParameters paramsFrom(Relation relation);

    public List<Relation> findOne(String startUri, String endUri) {
        return _find(pathBy(this.type), ImmutableMap.of("0",startUri,"1",endUri));
    }

    public List<Relation> findIn(org.librairy.model.domain.resources.Resource.Type type, String uri) {
        return _find(pathBy(type), ImmutableMap.of("0",uri));
    }

    public List<Relation> findAll() {
        return _find(pathBy(org.librairy.model.domain.resources.Resource.Type.ANY), ImmutableMap.of());
    }

    public Long countAll(){
        return _count(pathBy(org.librairy.model.domain.resources.Resource.Type.ANY),ImmutableMap.of());
    }

    public Long countIn(org.librairy.model.domain.resources.Resource.Type type, String uri){
        return _count(pathBy(type),ImmutableMap.of("0",uri));
    }

    public void deleteOne(String startUri, String endUri){
         _delete(pathBy(this.type), ImmutableMap.of("0",startUri,"1",endUri));
    }

    public void deleteAll(){
        _delete(pathBy(org.librairy.model.domain.resources.Resource.Type.ANY), ImmutableMap.of());
    }

    public void deleteIn(org.librairy.model.domain.resources.Resource.Type type, String uri){
        _delete(pathBy(type), ImmutableMap.of("0",uri));
    }

    public void save(Relation relation) {
        String relationLabel    = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type.key());
        String startNodeLabel   = StringUtils.capitalize(relation.getStartType().key());
        String endNodeLabel     = StringUtils.capitalize(relation.getEndType().key());

        TemplateParameters parameters = paramsFrom(relation);
        String extraParams = Strings.isNullOrEmpty(parameters.toExpression())? "": ","+parameters.toExpression();
        executor.execute("MATCH (a:"+startNodeLabel+"),(b:"+endNodeLabel+") WHERE a.uri = {0} AND b.uri = {1} CREATE (a)-[r:"+relationLabel+" { uri : {2}, creationTime : {3}, weight : {4} "+extraParams+" } ]->(b) RETURN r", parameters.getParams());
    }

    private void _delete(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" delete r").toString();
        executor.execute(query, params);
    }


    private List<Relation> _find(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" return r.uri,r.creationTime,s.uri,e.uri").toString();
        Optional<Result> result = executor.query(query, params);

        if (!result.isPresent()) return Collections.EMPTY_LIST;

        Iterator<Map<String, Object>> iterator = result.get().queryResults().iterator();
        List<Relation> relations = new ArrayList<>();
        while(iterator.hasNext()){
            Map<String, Object> relationship = iterator.next();

            try {
                Relation instance = (Relation) unifiedEdgeGraphRepositoryFactory.mappingOf(type).newInstance();
                instance.setUri((String) relationship.get("r.uri"));
                instance.setCreationTime((String) relationship.get("r.creationTime"));
                instance.setStartUri((String) relationship.get("s.uri"));
                instance.setEndUri((String) relationship.get("e.uri"));
                relations.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.error("Error reading relations by: " + query,e);
            }
        }
        return relations;
    }


    private long _count(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" return count(r)").toString();
        Optional<Result> result = executor.query(query, params);

        if (!result.isPresent()) return 0l;
        Object value = result.get().queryResults().iterator().next().get("count(r)");

        return Long.valueOf((int)value);
    }

}
