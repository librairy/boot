package org.librairy.storage.system.graph.template.nodes;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.librairy.storage.system.graph.template.TemplateExecutor;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<Relation> findOne(String startUri, String endUri) {
        throw new RuntimeException("Not implemented yet");
    }

    public List<Relation> findIn(org.librairy.model.domain.resources.Resource.Type type, String uri) {
        throw new RuntimeException("Not implemented yet");
    }

    public List<Relation> findAll() {
        throw new RuntimeException("Not implemented yet");
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
