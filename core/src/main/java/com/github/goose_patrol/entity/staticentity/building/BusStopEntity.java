package com.github.goose_patrol.entity.staticentity.building;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.staticentity.StaticEntity;
import com.github.goose_patrol.util.BoundingBox;

/** defines the bus stop entity */
public class BusStopEntity extends StaticEntity {

    public BusStopEntity(float posX, float posY, BoundingBox entityBoundingBox, GoosePatrol instance) {
        super(posX, posY, "Static/bus_stop.png", entityBoundingBox, instance);
    }

}
