package org.librairy.model.domain.resources;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(exclude = {"content","tokens"}, callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Part extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.PART;}

    public static final String SENSE="sense";
    private String sense;

    public static final String CONTENT="content";
    private String content;

    public static final String TOKENS="tokens";
    private String tokens;

}
