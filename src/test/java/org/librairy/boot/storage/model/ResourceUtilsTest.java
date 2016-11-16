/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.model;

import org.librairy.boot.model.domain.resources.Analysis;
import org.librairy.boot.model.utils.ResourceUtils;
import org.librairy.boot.storage.system.column.domain.AnalysisColumn;
import org.junit.Test;

/**
 * Created by cbadenes on 01/01/16.
 */
public class ResourceUtilsTest {

    @Test
    public void copy(){

        Analysis analysis = new Analysis();
        analysis.setConfiguration("conf1");
        analysis.setDescription("descr1");
        analysis.setDomain("domain1");
        analysis.setType("type1");
        analysis.setCreationTime("time1");
        analysis.setUri("uri1");

        System.out.println("Analysis: "+ analysis);

        AnalysisColumn wrapper = ResourceUtils.map(analysis, AnalysisColumn.class);

        System.out.println(wrapper);


    }

}
