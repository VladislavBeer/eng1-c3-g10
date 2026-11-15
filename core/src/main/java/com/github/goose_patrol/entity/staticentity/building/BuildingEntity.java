package com.github.goose_patrol.entity.staticentity.building;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

/** defines the building entity */
public class BuildingEntity extends StaticEntity {

    public BuildingEntity(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "Static/building.png", entityBoundingBox, instance);

    }

}
