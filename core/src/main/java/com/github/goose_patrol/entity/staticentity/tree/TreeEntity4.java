package com.github.goose_patrol.entity.staticentity.tree;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

public class TreeEntity4 extends StaticEntity {


    public TreeEntity4(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "static/Tree4.png", true, entityBoundingBox, instance);
    }

}
