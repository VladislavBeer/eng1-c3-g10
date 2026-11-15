package com.github.goose_patrol.entity.staticentity.tree;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

/** defines tree3 entity */
public class TreeEntity3 extends StaticEntity {

    public TreeEntity3(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "Static/Tree3.png", entityBoundingBox, instance);
    }

}
