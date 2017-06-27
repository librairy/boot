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
        if (relType.equals(Relation.Type.CONTAINS_TO_ITEM) && resType.equals(Resource.Type.ITEM))
            return "end";
        else if (relType.equals(Relation.Type.CONTAINS_TO_PART) && resType.equals(Resource.Type.PART))
            return "end";
        else if (relType.equals(Relation.Type.DEALS_WITH_FROM_ITEM) && resType.equals(Resource.Type.TOPIC))
            return "end";
        else if (relType.equals(Relation.Type.DEALS_WITH_FROM_PART) && resType.equals(Resource.Type.TOPIC))
            return "end";
        else if (relType.equals(Relation.Type.DESCRIBES) && resType.equals(Resource.Type.ITEM))
            return "end";
        return "start";

    }

    public static Relation.Type getRelationBetween(Resource.Type t1, Resource.Type t2){
        switch (t1){
            case ITEM:
                if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.CONTAINS_TO_ITEM;
                else if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.DEALS_WITH_FROM_ITEM;
                else if (t2.equals(Resource.Type.PART)) return Relation.Type.DESCRIBES;
                break;
            case PART:
                if (t2.equals(Resource.Type.ITEM)) return Relation.Type.DESCRIBES;
                else if (t2.equals(Resource.Type.TOPIC)) return Relation.Type.DEALS_WITH_FROM_PART;
                else if (t2.equals(Resource.Type.DOMAIN)) return Relation.Type.CONTAINS_TO_PART;
                break;
            case DOMAIN:
                if (t2.equals(Resource.Type.ITEM)) return Relation.Type.CONTAINS_TO_ITEM;
                else if (t2.equals(Resource.Type.PART)) return Relation.Type.CONTAINS_TO_PART;
                break;
            case TOPIC:
                if (t2.equals(Resource.Type.ITEM)) return Relation.Type.DEALS_WITH_FROM_ITEM;
                else if (t2.equals(Resource.Type.PART)) return Relation.Type.DEALS_WITH_FROM_PART;
                break;
        }
        throw new RuntimeException("Relation not handled between "+ t1 + " and " + t2);
    }

}
