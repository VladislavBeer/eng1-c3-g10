package com.github.goose_patrol.entity.staticentity;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.BoundingBox;

/** Defines a static entity to be used as scenery and are not interacted with */
public class StaticEntity extends Entity {

    public StaticEntity(float posX, float posY, String image, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, image, entityBoundingBox, instance);

    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean isMoving() {
        return false;
    }
}
