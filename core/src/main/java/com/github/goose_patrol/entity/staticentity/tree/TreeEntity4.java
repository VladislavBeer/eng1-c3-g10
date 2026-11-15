package com.github.goose_patrol.entity.staticentity.tree;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

/** defines tree4 entity */
public class TreeEntity4 extends StaticEntity {

    public TreeEntity4(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "Static/Tree4.png", entityBoundingBox, instance);
    }

}
