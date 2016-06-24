package org.librairy.storage.system.graph.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.librairy.model.domain.resources.Resource;
import org.librairy.storage.actions.RepeatableActionExecutor;
import org.librairy.storage.generator.URIGenerator;
import org.librairy.storage.system.graph.domain.nodes.Node;
import org.librairy.storage.system.graph.repository.nodes.UnifiedNodeGraphRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created on 09/04/16:
 *
 * @author cbadenes
 */
@Component
public class GraphCache extends RepeatableActionExecutor {


    @Autowired
    UnifiedNodeGraphRepositoryFactory nodeFactory;

    @Autowired
    URIGenerator uriGenerator;

    private LoadingCache<String, Node> cache;

    @PostConstruct
    public void setup(){
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, Node>() {
                            public Node load(String uri) {

                                Resource.Type type = uriGenerator.getResourceFrom
                                        (uri);


                                Optional<Object> node = performRetries(5, "Getting from cache: " + uri, () ->
                                        nodeFactory.repositoryOf(type)
                                                .findOneByUri(uri));
                                return (node.isPresent())? (Node) node.get() : null;
                            }
                        });

    }

    public void load(String uri, Node node){
        this.cache.put(uri,node);
    }

    public Node get(String uri){
        return this.cache.getUnchecked(uri);
    }

}
