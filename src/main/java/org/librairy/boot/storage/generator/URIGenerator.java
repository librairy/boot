/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.generator;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class URIGenerator implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(URIGenerator.class);

    private static final String SEPARATOR = "/";

    private static final SimpleDateFormat df;

    static{
        df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    }

    @Value("#{environment['LIBRAIRY_URI']?:'${librairy.uri}'}")
    String domainUri;

    String base;

    private static String baseUri = "";

    @PostConstruct
    public void setup(){
        base = "http://"+domainUri+"/";
        LOG.info("Uri Generator initialized successfully");
    }

    public static String compositeFromContent(Resource.Type resource1, String id1, Resource.Type resource2, String
            content ){
        initializeBaseUri();
        return new StringBuilder(baseUri)
                .append(resource1.route())
                .append(SEPARATOR)
                .append(id1)
                .append(SEPARATOR)
                .append(resource2.route())
                .append(SEPARATOR)
                .append(getMD5(content))
                .toString();
    }

    public static String fromContent(Resource.Type resource, String content){
        initializeBaseUri();
        return new StringBuilder(baseUri).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public static String compositionFromId(Resource.Type resource1, String id1, Resource.Type resource2, String id2){
        initializeBaseUri();
        return new StringBuilder(baseUri)
                .append(resource1.route())
                .append(SEPARATOR)
                .append(id1).append(SEPARATOR)
                .append(resource2.route())
                .append(SEPARATOR)
                .append(id2).toString();
    }

    public static String fromId(Resource.Type resource, String id){
        initializeBaseUri();
        return new StringBuilder(baseUri).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    private static void initializeBaseUri(){
        if (Strings.isNullOrEmpty(baseUri)){
            String librairyUri = System.getenv("LIBRAIRY_URI");
            baseUri = Strings.isNullOrEmpty(librairyUri)? "http://librairy.org/" : "http://"+librairyUri+"/";
        }
    }

    public String basedOnContent(Resource.Type resource, String content){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public String from(Resource.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String basedOnContent(Relation.Type resource, String content){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public String from(Relation.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String newFor(Resource.Type type){
        return from(type,df.format(new StringBuilder().append(getMD5(getUUID())).append(new Date()).toString()))
                .toString();
    }

    public String newFor(Relation.Type type){
        return from(type,df.format(new StringBuilder().append(getMD5(getUUID())).append(new Date()).toString()))
                .toString();
    }

    public static String retrieveId(String uri){
        return StringUtils.substringAfterLast(uri,"/");
    }

    public void setBase(String base){
        this.base = base;
    }

    private String getUUID(){
        return UUID.randomUUID().toString();
    }

    private static String getMD5(String text){
        String id;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(text.getBytes(),0,text.length());
            id = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            id = UUID.randomUUID().toString();
            LOG.warn("Error calculating MD5 from text. UUID will be used: " + id);
        }
        return id;
    }

    public static Resource.Type typeFrom(String uri){
        // "http://drinventor.eu/items/9c8b49fbc507cfe9903fc9f08dc2a8c8",

        String type = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(uri,"/"),"/");

        Optional<Resource.Type> resourceType = Arrays.stream(Resource.Type.values()).filter(entry -> type
                .equalsIgnoreCase
                        (entry.route())).findFirst();

        if (resourceType.isPresent()) return resourceType.get();
        throw new RuntimeException("No resource type found in uri: " + uri);
    }

    public static Relation.Type typeFromRelation(String uri){
        // "http://drinventor.eu/items/9c8b49fbc507cfe9903fc9f08dc2a8c8",

        String type = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(uri,"/"),"/");

        Optional<Relation.Type> relationType = Arrays.stream(Relation.Type.values()).filter(entry -> type
                .equalsIgnoreCase
                        (entry.route())).findFirst();

        if (relationType.isPresent()) return relationType.get();
        throw new RuntimeException("No resource type found in uri: " + uri);
    }

    public static BigInteger getId(String uri){
        String idString = StringUtils.substringAfterLast(uri,"/");
        BigInteger id = new BigInteger(idString, 16);
        return id;
    }


}
