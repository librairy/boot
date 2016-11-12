/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.generator;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.librairy.model.domain.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
public class URIGenerator {

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

    public static String apply(org.librairy.model.domain.resources.Resource.Type resource, String content){

        if (Strings.isNullOrEmpty(baseUri)){
            String librairyUri = System.getenv("LIBRAIRY_URI");
            baseUri = Strings.isNullOrEmpty(librairyUri)? "http://librairy.org/" : "http://"+librairyUri+"/";
        }

        return new StringBuilder(baseUri).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public String basedOnContent(org.librairy.model.domain.resources.Resource.Type resource, String content){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public String from(org.librairy.model.domain.resources.Resource.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String basedOnContent(org.librairy.model.domain.relations.Relation.Type resource, String content){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public String from(org.librairy.model.domain.relations.Relation.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String newFor(org.librairy.model.domain.resources.Resource.Type type){
        return from(type,df.format(new StringBuilder().append(getMD5(getUUID())).append(new Date()).toString()))
                .toString();
    }

    public String newFor(org.librairy.model.domain.relations.Relation.Type type){
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

    public static BigInteger getId(String uri){
        String idString = StringUtils.substringAfterLast(uri,"/");
        BigInteger id = new BigInteger(idString, 16);
        return id;
    }


}
