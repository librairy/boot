package org.librairy.storage.model;

import org.librairy.storage.system.column.domain.AnalysisColumn;
import org.junit.Test;

/**
 * Created by cbadenes on 01/01/16.
 */
public class ResourceUtilsTest {

    @Test
    public void copy(){

        org.librairy.model.domain.resources.Analysis analysis = new org.librairy.model.domain.resources.Analysis();
        analysis.setConfiguration("conf1");
        analysis.setDescription("descr1");
        analysis.setDomain("domain1");
        analysis.setType("type1");
        analysis.setCreationTime("time1");
        analysis.setUri("uri1");

        System.out.println("Analysis: "+ analysis);

        AnalysisColumn wrapper = org.librairy.model.utils.ResourceUtils.map(analysis, AnalysisColumn.class);

        System.out.println(wrapper);


    }

}
