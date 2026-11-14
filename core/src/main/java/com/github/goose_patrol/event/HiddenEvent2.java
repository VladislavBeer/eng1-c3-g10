package com.github.goose_patrol.event;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Goose;
import com.github.goose_patrol.util.Event;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.BoundingBox;

import java.util.Random;

public class HiddenEvent2 implements Event {

    private GoosePatrol instance;
    private boolean hasTriggeed = false;
    private boolean hasUsed = false;

    public HiddenEvent2(GoosePatrol instance) {
        this.instance = instance;
    }

    public void fire() {
        hasUsed = true;
    }

    public void activate() {
        if (hasTriggeed)
            return;
        Random randNum = new Random();
        for (int i = 0; i < 5; i++) {
            Goose goose = new Goose(randNum.nextFloat(850f, 950f),
                    randNum.nextFloat(650f, 750f), "goose/goosee.png", true, true, this.instance);
            this.instance.getEntityManager().addEntity(goose);
        }
        hasTriggeed = true;

    }

    // getter for the geese to read
    public boolean getHasUsed() {
        return hasUsed;
    }

}
