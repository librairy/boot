/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.boot.storage.documents;

import lombok.Data;

/**
 * Created by cbadenes on 25/02/16.
 */
@Data
public class Reference {

    String fileName;

    String uri;

    String title;

}