/*
 * Copyright (c) 2017. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.dao;

import org.junit.Test;

import java.util.StringTokenizer;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class ParametersDaoTest {


    @Test
    public void separator(){

        String mode = "lemma";

        StringTokenizer tokenizer = new StringTokenizer(mode,"+",false);


        while(tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            System.out.println("Token:  " + token);
        }


    }
}
