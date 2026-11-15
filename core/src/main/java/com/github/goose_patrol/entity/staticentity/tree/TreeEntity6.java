package com.github.goose_patrol.entity.staticentity.tree;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

/** defines tree6 entity */
public class TreeEntity6 extends StaticEntity {

    public TreeEntity6(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "Static/Tree6.png", entityBoundingBox, instance);
    }

}
