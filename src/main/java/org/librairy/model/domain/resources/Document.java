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
public class Document extends Resource {

    @Override
    public Resource.Type getResourceType() {return Type.DOCUMENT;}

    public static final String PUBLISHED_ON="publishedOn";
    private String publishedOn;

    public static final String PUBLISHED_BY="publishedBy";
    private String publishedBy;

    public static final String AUTHORED_ON="authoredOn";
    private String authoredOn;

    public static final String AUTHORED_BY="authoredBY";
    private String authoredBy;

    public static final String CONTRIBUTED_BY="contributedBy";
    private String contributedBy;

    public static final String RETRIEVED_FROM="retrievedFrom";
    private String retrievedFrom;

    public static final String RETRIEVED_ON="retrievedOn";
    private String retrievedOn;

    public static final String FORMAT="format";
    private String format;

    public static final String LANGUAGE="language";
    private String language;

    public static final String TITLE="title";
    private String title;

    public static final String SUBJECT="subject";
    private String subject;

    public static final String DESCRIPTION="description";
    private String description;

    public static final String RIGHTS="rights";
    private String rights;

    public static final String TYPE="type";
    private String type;

    public static final String CONTENT="content";
    private String content;

    public static final String TOKENS="tokens";
    private String tokens;
}
