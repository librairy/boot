package org.librairy.model.modules;

import lombok.Data;
import org.librairy.model.domain.relations.Relation;
import org.librairy.model.domain.resources.Resource;

/**
 * Created by cbadenes on 26/11/15.
 */
@Data
public class RoutingKey {

    String key;

    private RoutingKey(String key){
        this.key = key;
    }

    public static RoutingKey all(){
        return new RoutingKey("#");
    }

    public static RoutingKey of(Resource.Type resource, Resource.State state){
        return new RoutingKey(resource.name()+"."+state.key());
    }

    public static RoutingKey of(Relation.Type resource, Relation.State state){
        return new RoutingKey(resource.name()+"."+state.key());
    }

    public static RoutingKey of(String key){
        return new RoutingKey(key);
    }
}
