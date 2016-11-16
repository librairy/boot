/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.utils;

import org.librairy.boot.model.domain.relations.Relation;
import org.librairy.boot.model.domain.resources.Resource;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class RelationUtils {

    public static String getFieldFromRelation(Relation.Type relType, Resource.Type resType){
        if (relType.equals(Relation.Type.APPEARED_IN) && resType.equals(Resource.Type.DOMAIN))
            return "end";
        else if (relType.equals(Relation.Type.BUNDLES) && resType.equals(Resource.Type.ITEM))
            return "end";
        else if (relType.equals(Relation.Type.COMPOSES) && resType.equals(Resource.Type.DOMAIN))
            return "end";
        else if (relType.equals(Relation.Type.PROVIDES) && resType.equals(Resource.Type.DOCUMENT))
            return "end";
        else if (relType.equals(Relation.Type.CONTAINS_TO_DOCUMENT) && resType.equals(Resource.Type.DOCUMENT))
            return "end";
        else if (relType.equals(Relation.Type.CONTAINS_TO_ITEM) && resType.equals(Resource.Type.ITEM))
            return "end";
        else if (relType.equals(Relation.Type.CONTAINS_TO_PART) && resType.equals(Resource.Type.PART))
            return "end";
        else if (relType.equals(Relation.Type.DEALS_WITH_FROM_DOCUMENT) && resType.equals(Resource.Type.TOPIC))
            return "end";
        else if (relType.equals(Relation.Type.DEALS_WITH_FROM_ITEM) && resType.equals(Resource.Type.TOPIC))
            return "end";
        else if (relType.equals(Relation.Type.DEALS_WITH_FROM_PART) && resType.equals(Resource.Type.TOPIC))
            return "end";
        else if (relType.equals(Relation.Type.DESCRIBES) && resType.equals(Resource.Type.ITEM))
            return "end";
        else if (relType.equals(Relation.Type.EMBEDDED_IN) && resType.equals(Resource.Type.DOMAIN))
            return "end";
        else if (relType.equals(Relation.Type.EMERGES_IN) && resType.equals(Resource.Type.DOMAIN))
            return "end";
        else if (relType.equals(Relation.Type.MENTIONS_FROM_TERM) && resType.equals(Resource.Type.WORD))
            return "end";
        else if (relType.equals(Relation.Type.MENTIONS_FROM_TOPIC) && resType.equals(Resource.Type.WORD))
            return "end";
        return "start";

    }

    public static Relation.Type getRelationBetween(Resource.Type t1, Resource.Type t2){
        switch (t1){
            case SOURCE:
                if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.COMPOSES;
                else if (t2.equals(Resource.Type.DOCUMENT)) return Relation.Type.PROVIDES;
                break;
            case DOCUMENT:
                if (t2.equals(Resource.Type.SOURCE)) return Relation.Type.PROVIDES;
                else if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.CONTAINS_TO_DOCUMENT;
                else if (t2.equals(Resource.Type.ITEM)) return Relation.Type.BUNDLES;
                else if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.DEALS_WITH_FROM_DOCUMENT;
                break;
            case ITEM:
                if (t2.equals(Resource.Type.DOCUMENT)) return Relation.Type.BUNDLES;
                else if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.CONTAINS_TO_ITEM;
                else if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.DEALS_WITH_FROM_ITEM;
                else if (t2.equals(Resource.Type.PART)) return Relation.Type.DESCRIBES;
                break;
            case PART:
                if (t2.equals(Resource.Type.ITEM)) return Relation.Type.DESCRIBES;
                else if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.DEALS_WITH_FROM_PART;
                else if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.CONTAINS_TO_PART;
                break;
            case DOMAIN:
                if (t2.equals(Resource.Type.SOURCE)) return Relation.Type.COMPOSES;
                else if (t2.equals(Resource.Type.DOCUMENT)) return Relation.Type.CONTAINS_TO_DOCUMENT;
                else if (t2.equals(Resource.Type.ITEM)) return Relation.Type.CONTAINS_TO_ITEM;
                else if (t2.equals(Resource.Type.PART)) return Relation.Type.CONTAINS_TO_PART;
                else if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.EMERGES_IN;
                else if (t2.equals(Resource.Type.TERM)) return Relation.Type.APPEARED_IN;
                else if (t2.equals(Resource.Type.WORD)) return Relation.Type.EMBEDDED_IN;
                break;
            case TOPIC:
                if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.EMERGES_IN;
                else if (t2.equals(Resource.Type.WORD)) return Relation.Type.MENTIONS_FROM_TOPIC;
                else if (t2.equals(Resource.Type.DOCUMENT)) return Relation.Type.DEALS_WITH_FROM_DOCUMENT;
                else if (t2.equals(Resource.Type.ITEM)) return Relation.Type.DEALS_WITH_FROM_ITEM;
                else if (t2.equals(Resource.Type.PART)) return Relation.Type.DEALS_WITH_FROM_PART;
                break;
            case WORD:
                if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.MENTIONS_FROM_TOPIC;
                else if (t2.equals(Resource.Type.TERM)) return Relation.Type.MENTIONS_FROM_TERM;
                else if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.EMBEDDED_IN;
                break;
            case TERM:
                if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.APPEARED_IN;
                else if (t2.equals(Resource.Type.WORD)) return Relation.Type.MENTIONS_FROM_TERM;
                break;
        }
        throw new RuntimeException("Relation not handled between "+ t1 + " and " + t2);
    }

}
