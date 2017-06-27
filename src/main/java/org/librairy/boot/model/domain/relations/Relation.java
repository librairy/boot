/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.librairy.boot.model.domain.LinkableElement;
import org.librairy.boot.model.domain.resources.Resource;
import org.librairy.boot.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Created by cbadenes on 22/12/15.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, exclude = {"startUri","endUri","weight"},callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class Relation extends LinkableElement {

    private static final Logger LOG = LoggerFactory.getLogger(Relation.class);

    @Getter @Setter
    public Long id;

    @Getter @Setter
    protected String startUri;

    @Getter @Setter
    protected String endUri;

    @Getter @Setter
    protected Double weight;

    public void between(String startUri,String endUri){
        this.startUri = startUri;
        this.endUri = endUri;
    }

    public void between(LinkableElement start,LinkableElement end){
        this.startUri = start.getUri();
        this.endUri = end.getUri();
    }

    public Resource.Type getStartType(){
        return Resource.Type.ANY;
    }

    public Resource.Type getEndType(){
        return Resource.Type.ANY;
    }

    public Relation.Type getType(){
        return Relation.Type.ANY;
    }

    public enum Type{
        CONTAINS_TO_DOMAIN("contains","contains"),
        CONTAINS_TO_ITEM("contains","contains"),
        CONTAINS_TO_PART("contains","contains"),
        SIMILAR_TO_ITEMS("similarTo","similarities"),
        SIMILAR_TO_PARTS("similarTo","similarities"),
        DEALS_WITH_FROM_ITEM("dealsWith","deals"),
        DEALS_WITH_FROM_PART("dealsWith","deals"),
        DESCRIBES("describes","descriptions"),
        EMERGES_IN("emergesIn","emerges"),
        ANY("*","*");

        private final String route;
        private final String key;

        public String route(){
            return route;
        }

        public String key() { return key; }

        Type(String key, String route){
            this.key    = key;
            this.route = route;
        }
    }

    public enum State {
        CREATED("created"),
        UPDATED("updated"),
        DELETED("deleted"),
        ANY("*");

        String keyValue;

        State(String key){ keyValue = key;}

        public String key(){ return keyValue;}
    }

    public static Class classOf(Type type){
        switch (type){
            case CONTAINS_TO_ITEM: return ContainsItem.class;
            case CONTAINS_TO_PART: return ContainsPart.class;
            case DEALS_WITH_FROM_ITEM: return DealsWithFromItem.class;
            case DEALS_WITH_FROM_PART: return DealsWithFromPart.class;
            case DESCRIBES: return Describes.class;
            case EMERGES_IN: return EmergesIn.class;
            case SIMILAR_TO_ITEMS: return SimilarToItems.class;
            case SIMILAR_TO_PARTS: return SimilarToParts.class;
            default: return Relation.class;
        }
    }

    public static Contains newContains(String startUri, String endUri){

        switch(URIGenerator.typeFrom(endUri)){
            case ITEM: return newRelation(ContainsItem.class,startUri,endUri);
            case PART: return newRelation(ContainsPart.class,startUri,endUri);
            case DOMAIN: return newRelation(ContainsDomain.class,startUri,endUri);
            default: throw new RuntimeException("Invalid start uri for CONTAINS relation: " + startUri);
        }

    }

    public static DealsWithFromItem newDealsWithFromItem(String startUri, String endUri){
        return newRelation(DealsWithFromItem.class,startUri,endUri);
    }

    public static DealsWithFromPart newDealsWithFromPart(String startUri, String endUri){
        return newRelation(DealsWithFromPart.class,startUri,endUri);
    }

    public static Describes newDescribes(String startUri, String endUri){
        return newRelation(Describes.class,startUri,endUri);
    }

    public static EmergesIn newEmergesIn(String startUri, String endUri){
        return newRelation(EmergesIn.class,startUri,endUri);
    }

    public static SimilarToItems newSimilarToItems(String startUri, String endUri, String domainUri){
        SimilarToItems rel = newRelation(SimilarToItems.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    public static SimilarToParts newSimilarToParts(String startUri, String endUri, String domainUri){
        SimilarToParts rel = newRelation(SimilarToParts.class, startUri, endUri);
        rel.setDomain(domainUri);
        return rel;
    }

    private static <T extends Relation> T newRelation(Class<T> clazz, String su, String eu){
        T instance = null;
        try {
            instance = clazz.newInstance();
            instance.setStartUri(su);
            instance.setEndUri(eu);
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Error creating instance of a relation: " + clazz);
        }
        return instance;
    }

    public Contains asContains(){
        return Contains.class.cast(this);
    }


    public DealsWithFromItem asDealsWithFromItem(){
        return DealsWithFromItem.class.cast(this);
    }

    public DealsWithFromPart asDealsWithFromPart(){
        return DealsWithFromPart.class.cast(this);
    }

    public Describes asDescribes(){
        return Describes.class.cast(this);
    }

    public EmergesIn asEmergesIn(){
        return EmergesIn.class.cast(this);
    }


    public SimilarToItems asSimilarToItems(){
        return SimilarToItems.class.cast(this);
    }

    public SimilarToParts asSimilarToParts(){
        return SimilarToParts.class.cast(this);
    }

}
