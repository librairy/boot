package org.librairy.storage.system.graph.template.nodes;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.model.utils.ResourceUtils;
import org.librairy.storage.system.column.repository.UnifiedColumnRepository;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by cbadenes on 28/02/16.
 */
public abstract class NodeTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(NodeTemplate.class);

    protected final Resource.Type type;

    @Autowired
    TemplateExecutor executor;

    @Autowired
    UnifiedNodeGraphRepositoryFactory unifiedNodeGraphRepositoryFactory;

    public NodeTemplate(Resource.Type type){
        this.type = type;
    }


    public Resource.Type accept() {
        return type;
    }

    public abstract String pathTo(Resource.Type type);

    public void save(Resource resource){
        String query = "create (n:"+typeId()+" { uri: {0} , creationTime: {1} })";
        Map params = ImmutableMap.of("0",resource.getUri(), "1", resource.getCreationTime());
        executor.execute(query, params);
    }

    public List<Resource> findFrom(Resource.Type type, String uri) {
        String query = "match " + pathTo(type) + " return n";
        Map params = ImmutableMap.of("0",uri);
        return _find(query,params);
    }

    public List<Resource> findAll() {
        String query = "match (n:"+typeId()+") return n";
        return _find(query, ImmutableMap.of());
    }

    private List<Resource> _find(String query, Map params){

        Optional<Result> results = executor.query(query, params);

        if (!results.isPresent()) return Collections.EMPTY_LIST;

        Iterator<Map<String, Object>> iterator = results.get().queryResults().iterator();
        List<Resource> resources = new ArrayList<>();
        while(iterator.hasNext()){
            Map<String, Object> resource = iterator.next();
            resources.add((Resource) resource.get("n"));
        }

        return resources;
    }

    public Long countAll(){
        String query = new StringBuilder().append("match (n: "+typeId()+") return count(n)").toString();
        Optional<Result> result = executor.query(query, ImmutableMap.of());

        if (!result.isPresent()) return 0l;
        Object value = result.get().queryResults().iterator().next().get("count(n)");

        return Long.valueOf((int)value);
    }

    public Long countIn(org.librairy.model.domain.resources.Resource.Type type, String uri){
        throw new RuntimeException("Not implemented yet");
    }


    public Optional<Resource> fromNodeId(Long id){
        String query = "match (n) where ID(n)="+id+" return n.uri, n.creationTime";
        Optional<Result> result = executor.query(query, ImmutableMap.of());

        if (!result.isPresent()) return Optional.empty();
        Map<String, Object> resultNode = result.get().queryResults().iterator().next();
        Resource resource = new Resource();
        resource.setUri((String)resultNode.get("n.uri"));
        resource.setCreationTime((String)resultNode.get("n.creationTime"));

        return Optional.of(resource);
    }

    public void deleteOne(String uri){
        _delete( "(n: {uri: {0} })", ImmutableMap.of("0",uri));
    }

    public void deleteAll(){
        _delete( "(n: " + typeId()+")", ImmutableMap.of());
    }

    public void deleteIn(org.librairy.model.domain.resources.Resource.Type type, String uri){
        throw new RuntimeException("Not implemented yet");
    }

    private void _delete(String path, Map params){
        String query = new StringBuilder().append("match ").append(path).append(" detach delete n").toString();
        executor.execute(query, params);
    }

    protected String typeId(){
        return StringUtils.capitalize(type.name().toLowerCase());
    }


}
