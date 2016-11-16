/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.model.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cbadenes on 04/01/16.
 */
public class TimeUtils {

    private static final SimpleDateFormat df;

    static{
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    }

    public static String asISO(){
        return df.format(new Date());
    }
}
