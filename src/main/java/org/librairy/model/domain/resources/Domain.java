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
public class Domain extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.DOMAIN;}

    public static final String NAME="name";
    private String name;

    public static final String DESCRIPTION="description";
    private String description;

}
