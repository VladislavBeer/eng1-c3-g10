package com.github.goose_patrol.event;

import com.github.goose_patrol.entity.Goose;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.Event;

import java.util.Random;

public class HiddenEvent1 implements Event {

    private GoosePatrol instance;
    private boolean hasTriggeed = false;

    public HiddenEvent1(GoosePatrol instance) {
        this.instance = instance;
    }

    public void fire() {
        if (hasTriggeed)
            return;
        Random randNum = new Random();
        for (int i = 0; i < 5; i++) {
            Goose goose = new Goose(randNum.nextFloat(850f, 950f),
                    randNum.nextFloat(450f, 550f), "goose/goosee.png", true, true, this.instance);
            this.instance.getEntityManager().addEntity(goose);
        }
        hasTriggeed = true;

    }

}
