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
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Filter extends Resource {

	@Override
	public Resource.Type getResourceType() {return Type.FILTER;}

	public static final String CONTENT="content";
	private String content = "";


}
