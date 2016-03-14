package org.librairy.storage.generator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Value("${librairy.uri.base}")
    String base;

    public String basedOnContent(org.librairy.model.domain.resources.Resource.Type resource, String content){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(getMD5(content)).toString();
    }

    public String from(org.librairy.model.domain.resources.Resource.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String from(org.librairy.model.domain.relations.Relation.Type resource, String id){
        return new StringBuilder(base).append(resource.route()).append(SEPARATOR).append(id).toString();
    }

    public String newFor(org.librairy.model.domain.resources.Resource.Type type){
        return from(type,df.format(new Date())+"-"+getMD5(getUUID())).toString();
    }

    public String newFor(org.librairy.model.domain.relations.Relation.Type type){
        return from(type,df.format(new Date())+"-"+getMD5(getUUID())).toString();
    }

    private String getUUID(){
        return UUID.randomUUID().toString();
    }

    private String getMD5(String text){
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

    public static BigInteger getId(String uri){
        String idString = StringUtils.substringAfterLast(uri,"/");
        BigInteger id = new BigInteger(idString, 16);
        return id;
    }

}
