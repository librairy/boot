package org.librairy.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Topic extends Resource {

    @Override
    public Type getResourceType() {return Type.TOPIC;}

    public static final String CONTENT="content";
    private String content;

    public static final String ANALYSIS="analysis";
    private String analysis;
}
