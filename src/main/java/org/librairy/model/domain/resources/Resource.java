/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.resources;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.librairy.model.domain.LinkableElement;

@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public class Resource extends LinkableElement {

	public Type getResourceType(){
		return Type.ANY;
	}

	public enum Type{
		SOURCE("source","sources"),
		DOMAIN("domain","domains"),
		DOCUMENT("document","documents"),
		ITEM("item","items"),
		PART("part","parts"),
		WORD("word","words"),
		ANALYSIS("analysis","analyses"),
		TOPIC("topic","topics"),
		SERIALIZED_OBJECT("object","objects"),
		TERM("term","terms"),
		FILTER("filter","filters"),
		PATH("path","paths"),
		FILE("file","files"),
		ANY("*","*");

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
			case ANALYSIS:return Analysis.class;
			case DOCUMENT: return org.librairy.model.domain.resources.Document.class;
			case DOMAIN: return org.librairy.model.domain.resources.Domain.class;
			case ITEM: return org.librairy.model.domain.resources.Item.class;
			case PART: return org.librairy.model.domain.resources.Part.class;
			case SERIALIZED_OBJECT: return org.librairy.model.domain.resources.SerializedObject.class;
			case SOURCE: return org.librairy.model.domain.resources.Source.class;
			case TERM: return org.librairy.model.domain.resources.Term.class;
			case TOPIC: return Topic.class;
			case WORD: return org.librairy.model.domain.resources.Word.class;
			case FILTER: return org.librairy.model.domain.resources.Filter.class;
			case PATH: return org.librairy.model.domain.resources.Path.class;
			default: return Resource.class;
		}
	}

	public static Analysis newAnalysis(){
		return new Analysis();
	}

	public static org.librairy.model.domain.resources.Document newDocument(String title){
		Document document = new Document();
		document.setTitle(title);
		return document;
	}

	public static org.librairy.model.domain.resources.Document newDocument(){
		Document document = new Document();
		return document;
	}

	public static org.librairy.model.domain.resources.Domain newDomain(String name){
		Domain domain = new Domain();
		domain.setName(name);
		return domain;
	}

	public static org.librairy.model.domain.resources.Item newItem(String content){
		Item item = new Item();
		item.setContent(content);
		item.setTokens("");
		return item;
	}

	public static org.librairy.model.domain.resources.Part newPart(String content){
		Part part =  new org.librairy.model.domain.resources.Part();
		part.setContent(content);
		part.setTokens("");
		return part;
	}

	public static org.librairy.model.domain.resources.Source newSource(String name){
		Source source = new Source();
		source.setName(name);
		return source;
	}

	public static org.librairy.model.domain.resources.Term newTerm(String content){
		Term term =  new org.librairy.model.domain.resources.Term();
		term.setContent(content);
		return term;
	}

	public static org.librairy.model.domain.resources.Filter newFilter(String content){
		Filter filter = new Filter();
		filter.setContent(content);
		return filter;
	}

	public static org.librairy.model.domain.resources.Path newPath(String start, String end){
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

	public static org.librairy.model.domain.resources.Word newWord(String content){
		Word word = new Word();
		word.setContent(content);
		return word;
	}

	public Analysis asAnalysis(){
		return Analysis.class.cast(this);
	}

	public org.librairy.model.domain.resources.Document asDocument(){
		return org.librairy.model.domain.resources.Document.class.cast(this);
	}

	public org.librairy.model.domain.resources.Domain asDomain(){
		return org.librairy.model.domain.resources.Domain.class.cast(this);
	}

	public org.librairy.model.domain.resources.Item asItem(){
		return org.librairy.model.domain.resources.Item.class.cast(this);
	}

	public org.librairy.model.domain.resources.Part asPart(){
		return org.librairy.model.domain.resources.Part.class.cast(this);
	}

	public org.librairy.model.domain.resources.SerializedObject asSerializedObject(){
		return org.librairy.model.domain.resources.SerializedObject.class.cast(this);
	}

	public org.librairy.model.domain.resources.Source asSource(){
		return org.librairy.model.domain.resources.Source.class.cast(this);
	}

	public org.librairy.model.domain.resources.Term asTerm(){
		return org.librairy.model.domain.resources.Term.class.cast(this);
	}

	public Topic asTopic(){
		return Topic.class.cast(this);
	}

	public org.librairy.model.domain.resources.Word asWord(){
		return org.librairy.model.domain.resources.Word.class.cast(this);
	}

	public org.librairy.model.domain.resources.Filter asFilter(){
		return org.librairy.model.domain.resources.Filter.class.cast(this);
	}

	public org.librairy.model.domain.resources.Path asPath(){
		return org.librairy.model.domain.resources.Path.class.cast(this);
	}

}
