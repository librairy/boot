package org.librairy.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 12/01/16.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class User extends Resource {

    @Override
    //TODO to be included in Resource.Type
    public Resource.Type getResourceType() {return Type.ANY;}

    String name;

    String surname;
}
