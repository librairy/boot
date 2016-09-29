/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.storage.system.column.template;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.librairy.Config;
import org.librairy.storage.system.column.templates.ColumnTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created on 08/09/16:
 *
 * @author cbadenes
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ColumnTemplateTest {

    @Autowired
    ColumnTemplate columnTemplate;


    @Test
    public void deleteSimilars(){
        String domainUri = "http://librairy.org/domains/default";
        columnTemplate.execute("delete from research.similarto where domain = '"+domainUri+"';");
    }

}
