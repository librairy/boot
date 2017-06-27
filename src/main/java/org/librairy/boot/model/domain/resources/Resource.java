/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.domain.resources;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.boot.model.domain.LinkableElement;

@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Resource extends LinkableElement {

	public Type getResourceType(){
		return Type.ANY;
	}

	public enum Type{
		DOMAIN("domain","domains"),
		ITEM("item","items"),
		PART("part","parts"),
		TOPIC("topic","topics"),
		FILTER("filter","filters"),
		PATH("path","paths"),
		LISTENER("listener","listeners"),
		ANNOTATION("annotation","annotations"),
		ANY("*","*"), ;

		String plural;
		String key;

		Type(String key, String plural){
			this.key = key;
			this.plural = plural;
		}

		public String key(){ return key;}

		public String route(){ return plural;}
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
		switch(type){
			case DOMAIN: return Domain.class;
			case ITEM: return Item.class;
			case PART: return Part.class;
			case TOPIC: return Topic.class;
			case FILTER: return Filter.class;
			case PATH: return Path.class;
			case LISTENER: return Listener.class;
			case ANNOTATION: return Annotation.class;
			default: return Resource.class;
		}
	}

	public static Domain newDomain(String name){
		Domain domain = new Domain();
		domain.setName(name);
		return domain;
	}

	public static Item newItem(String content){
		Item item = new Item();
		item.setContent(content);
		item.setTokens("");
		return item;
	}

	public static Part newPart(String content){
		Part part =  new Part();
		part.setContent(content);
		part.setTokens("");
		return part;
	}

	public static Filter newFilter(String content){
		Filter filter = new Filter();
		filter.setContent(content);
		return filter;
	}

	public static Path newPath(String start, String end){
		Path path = new Path();
		path.setStart(start);
		path.setEnd(end);
		return path;
	}

	public static Topic newTopic(String content){
		Topic topic = new Topic();
		topic.setContent(content);
		return topic;
	}

	public static Listener newListener(String route){
		Listener listener = new Listener();
		listener.setRoute(route);
		return listener;
	}

	public static Annotation newAnnotation(String resource, String type){
		Annotation annotation = new Annotation();
		annotation.setResource(resource);
		annotation.setType(type);
		return annotation;
	}


	public Domain asDomain(){
		return Domain.class.cast(this);
	}

	public Item asItem(){
		return Item.class.cast(this);
	}

	public Part asPart(){
		return Part.class.cast(this);
	}

	public Topic asTopic(){
		return Topic.class.cast(this);
	}

	public Filter asFilter(){
		return Filter.class.cast(this);
	}

	public Path asPath(){
		return Path.class.cast(this);
	}

	public Listener asListener(){
		return Listener.class.cast(this);
	}

	public Annotation asAnnotation(){
		return Annotation.class.cast(this);
	}
}
