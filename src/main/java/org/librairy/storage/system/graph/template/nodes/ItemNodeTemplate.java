package org.librairy.storage.system.graph.template.nodes;

import org.librairy.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class ItemNodeTemplate extends NodeTemplate {

    public ItemNodeTemplate() {
        super(Resource.Type.ITEM);
    }
}
