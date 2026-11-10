package com.github.goose_patrol.entity;

public class Goose extends Entity {

    private boolean triggered;

    

    public Goose(float posX, float posY, String image, boolean active, boolean triggered) {
        super(posX, posY, image, active, null);
        this.triggered = triggered;
    }

    public boolean trigger() {

        // TODO
        return false;

    }

    public void kill() {



    }

    @Override
    public void tickEntity() {

        super.tickEntity();

    }

}
