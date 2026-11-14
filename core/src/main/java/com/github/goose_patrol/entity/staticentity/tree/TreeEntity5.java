package com.github.goose_patrol.entity.staticentity.tree;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

/** defines tree5 entity */
public class TreeEntity5 extends StaticEntity {

    public TreeEntity5(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "static/Tree5.png", entityBoundingBox, instance);
    }

}
