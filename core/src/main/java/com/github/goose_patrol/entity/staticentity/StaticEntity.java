package com.github.goose_patrol.entity.staticentity;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.BoundingBox;

public class StaticEntity extends Entity {


    public StaticEntity(float posX, float posY, String image, boolean active, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, image, active, entityBoundingBox, instance);
}
}
    

