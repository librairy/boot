package org.librairy.storage.system.graph.template.edges;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.system.graph.repository.edges.UnifiedEdgeGraphRepositoryFactory;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.neo4j.ogm.model.Property;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.response.model.RelationshipModel;
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

    protected abstract String label();

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

    public void delete(String uri){
        _delete( "(a)-[r: " + label() + " {uri: {0} }]->(b)", ImmutableMap.of("0",uri));
    }

    public void save(Relation relation) {
        String relationLabel    = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type.key());
        String startNodeLabel   = StringUtils.capitalize(relation.getStartType().key());
        String endNodeLabel     = StringUtils.capitalize(relation.getEndType().key());

        TemplateParameters parameters = paramsFrom(relation);
        String extraParams = Strings.isNullOrEmpty(parameters.toExpression())? "": ","+parameters.toExpression();
        Map<String, Object> params = parameters.getParams();
        executor.execute("MATCH (a:"+startNodeLabel+"),(b:"+endNodeLabel+") WHERE a.uri = {0} AND b.uri = {1} CREATE " +
                "(a)-[r:"+relationLabel+" { uri : {2}, creationTime : {3}, weight : {4} "+extraParams+" } ]->(b) " +
                "RETURN r", params);


        // TODO This should be removed when Neo4j uses Bolt
        // Remove duplicated relations
        String queryRels = "MATCH (a:"+startNodeLabel+")-[r:"+relationLabel+"]->(b:"+endNodeLabel+") WHERE a.uri = " +
                "{0} AND b.uri = {1} AND r.uri = {2} return ID(r),r.uri,r.creationTime";
        Optional<Result> result = executor.query(queryRels, params);
        if (!result.isPresent()) return;
        Iterator<Map<String, Object>> it = result.get().queryResults().iterator();
        int counter = 0;
        while(it.hasNext()){
            Map<String, Object> resultNode = it.next();
            boolean matched = ((String) resultNode.get("r.creationTime")).equalsIgnoreCase((String) params.get("3"));
            if (!matched){
                // remove
                executor.execute("start r=rel("+( (Integer) resultNode.get("ID(r)"))+") delete r", ImmutableMap.of());
            }else if (matched && counter>0){
                // remove
                executor.execute("start r=rel("+( (Integer) resultNode.get("ID(r)"))+") delete r", ImmutableMap.of());
            }else{
                counter += 1;
            }
        }

    }

    private void _delete(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" delete r").toString();
        executor.execute(query, params);
    }

    public Optional<Relation> fromNodeId(Long id){
        String query = "match (s)-[r]->(e) where ID(r)="+id+" return r.uri, r.creationTime, r.weight, s.uri, e.uri";
        Optional<Result> result = executor.query(query, ImmutableMap.of());

        if (!result.isPresent()) return Optional.empty();
        Map<String, Object> resultNode = result.get().queryResults().iterator().next();
        Relation relation = new Relation();
        relation.setUri((String)resultNode.get("r.uri"));
        relation.setCreationTime((String)resultNode.get("r.creationTime"));
        relation.setStartUri((String) resultNode.get("s.uri"));
        relation.setEndUri((String) resultNode.get("e.uri"));
        relation.setId(id);
        Object weight = resultNode.get("r.weight");
        if (weight != null){
            relation.setWeight((Double)weight);
        }

        return Optional.of(relation);
    }

    private List<Relation> _find(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" return ID(r),r.uri,r.creationTime,s" +
                ".uri,e" +
                ".uri,r.weight").toString();
        Optional<Result> result = executor.query(query, params);

        if (!result.isPresent()) return Collections.EMPTY_LIST;

        Iterator<Map<String, Object>> iterator = result.get().queryResults().iterator();
        List<Relation> relations = new ArrayList<>();
        while(iterator.hasNext()){
            Map<String, Object> relationship = iterator.next();

            try {
                Relation instance = (Relation) unifiedEdgeGraphRepositoryFactory.mappingOf(type).newInstance();
                instance.setId(Long.valueOf((Integer) relationship.get("ID(r)")));
                instance.setUri((String) relationship.get("r.uri"));
                instance.setCreationTime((String) relationship.get("r.creationTime"));
                instance.setStartUri((String) relationship.get("s.uri"));
                instance.setEndUri((String) relationship.get("e.uri"));
                instance.setWeight((Double) relationship.get("r.weight"));
                relations.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.error("Error reading relations by: " + query,e);
            }
        }
        return relations;
    }

//    public Optional<Relation> read(String uri){
//
//        String query = new StringBuilder().append("match (a)-[r:").append(label()).append(" {uri:{0} }]->(b) return " +
//                "a.uri, b.uri, r").toString();
//        Optional<Result> result = executor.query(query, ImmutableMap.of("0",uri));
//
//        if (!result.isPresent()) return Optional.empty();
//
//        Iterator<Map<String, Object>> iterator = result.get().queryResults().iterator();
//        List<Relation> relations = new ArrayList<>();
//        while(iterator.hasNext()){
//            Map<String, Object> relationship = iterator.next();
//
//            try {
//                RelationshipModel element = (RelationshipModel) relationship.get("r");
//
//                Relation instance = (Relation) unifiedEdgeGraphRepositoryFactory.mappingOf(type).newInstance();
//                instance.setStartUri((String) relationship.get("a.uri"));
//                instance.setEndUri((String) relationship.get("b.uri"));
//
//
//
//                for (Property<String, Object> property : element.getPropertyList()) {
//
//                    unifiedEdgeGraphRepositoryFactory.mappingOf(type).getMethod()
//
//                    property.getKey()
//
//                }
//
//                instance.setUri((String) relationship.get("r.uri"));
//                instance.setCreationTime((String) relationship.get("r.creationTime"));
//                instance.setWeight((Double) relationship.get("r.weight"));
////                relations.add(instance);
//            } catch (InstantiationException | IllegalAccessException e) {
//                LOG.error("Error reading relations by: " + query,e);
//            }
//        }
//
//
//        return Optional.empty();
//    }


    private long _count(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" return count(r)").toString();
        Optional<Result> result = executor.query(query, params);

        if (!result.isPresent()) return 0l;
        Object value = result.get().queryResults().iterator().next().get("count(r)");

        return Long.valueOf((int)value);
    }

}
