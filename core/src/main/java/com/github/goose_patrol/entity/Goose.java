package com.github.goose_patrol.entity;

import com.github.goose_patrol.GoosePatrol;

public class Goose extends Entity {

    private boolean triggered;

    

    public Goose(float posX, float posY, String image, boolean active, boolean triggered, GoosePatrol instance) {
        super(posX, posY, image, active, null, instance);
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
