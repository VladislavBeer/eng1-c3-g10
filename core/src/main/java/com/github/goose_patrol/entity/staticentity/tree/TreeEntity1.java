package com.github.goose_patrol.entity.staticentity.tree;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

public class TreeEntity1 extends StaticEntity {


    public TreeEntity1(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "static/Tree1.png",  entityBoundingBox, instance);
    }

}
