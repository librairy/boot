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
			case DOCUMENT: return Document.class;
			case DOMAIN: return Domain.class;
			case ITEM: return Item.class;
			case PART: return Part.class;
			case SERIALIZED_OBJECT: return SerializedObject.class;
			case SOURCE: return Source.class;
			case TERM: return Term.class;
			case TOPIC: return Topic.class;
			case WORD: return Word.class;
			case FILTER: return Filter.class;
			case PATH: return Path.class;
			default: return Resource.class;
		}
	}

	public static Analysis newAnalysis(){
		return new Analysis();
	}

	public static Document newDocument(String title){
		Document document = new Document();
		document.setTitle(title);
		return document;
	}

	public static Document newDocument(){
		Document document = new Document();
		return document;
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

	public static Source newSource(String name){
		Source source = new Source();
		source.setName(name);
		return source;
	}

	public static Term newTerm(String content){
		Term term =  new Term();
		term.setContent(content);
		return term;
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

	public static Word newWord(String content){
		Word word = new Word();
		word.setContent(content);
		return word;
	}

	public Analysis asAnalysis(){
		return Analysis.class.cast(this);
	}

	public Document asDocument(){
		return Document.class.cast(this);
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

	public SerializedObject asSerializedObject(){
		return SerializedObject.class.cast(this);
	}

	public Source asSource(){
		return Source.class.cast(this);
	}

	public Term asTerm(){
		return Term.class.cast(this);
	}

	public Topic asTopic(){
		return Topic.class.cast(this);
	}

	public Word asWord(){
		return Word.class.cast(this);
	}

	public Filter asFilter(){
		return Filter.class.cast(this);
	}

	public Path asPath(){
		return Path.class.cast(this);
	}

}
