/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
@XmlRootElement(name = "path")
@XmlAccessorType(XmlAccessType.FIELD)
public class Path extends Resource {

	@Override
	public Type getResourceType() {return Type.PATH;}

	public static final String START="start";
	private String start = "";

	public static final String END="end";
	private String end = "";

}
