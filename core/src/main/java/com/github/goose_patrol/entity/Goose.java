package com.github.goose_patrol.entity;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.BoundingBox;

public class Goose extends Entity {

    private boolean triggered;

    public Goose(float posX, float posY, String image, boolean active, boolean triggered, GoosePatrol instance) {
        super(posX, posY, image, active, new BoundingBox(posX, posX + 50, posY, posY + 50), instance);
        this.triggered = triggered;
    }

    public boolean trigger() {

        // TODO
        return false;

    }

    public void kill() {

    }

    @Override
    public void tickEntity(float delta) {

        super.tickEntity(delta);

    }

}
