package org.librairy.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.relations.Relation;
import org.librairy.storage.system.graph.domain.nodes.Node;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 02/02/16.
 */
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = false)
@ToString(of={"uri"}, callSuper = true)
public abstract class Edge<S extends Node,E extends Node> extends Relation{

    @GraphId
    protected Long id;

    @Property
    protected String uri;

    @Property
    private Double weight;

    @Property
    private String creationTime;

    @StartNode
    protected S startNode;

    @EndNode
    protected E endNode;

    public void setStart(Node node){
        this.startNode = (S) node;
        this.startUri = node.getUri();
    }

    public S getStart(){
        return this.startNode;
    }

    public void setEnd(Node node){
        this.endNode = (E) node;
        this.endUri = node.getUri();
    }

    public E getEnd(){
        return this.endNode;
    }

    @Override
    public String getStartUri(){
        return startUri;
    }

    @Override
    public String getEndUri(){
        return endUri;
    }

    @Override
    public Relation.Type getType(){
        return Type.ANY;
    }

}
