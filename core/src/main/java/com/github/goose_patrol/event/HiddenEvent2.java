package com.github.goose_patrol.event;

import com.badlogic.gdx.math.Vector2;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Goose;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.util.Event;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.BoundingBox;
import java.util.List;

import java.util.Random;

/**
 * The second hidden event, triggered on the bottom left corner of the map,
 * which will spawn 5 geese and must be beaten using the phone item
 * <p>
 * Contains methods that add geese to the entity manager when activated and
 * changes the geese's behaviour when the phone item is used
 */
public class HiddenEvent2 implements Event {

    private GoosePatrol instance;
    private boolean hasTriggeed = false;
    private boolean hasUsed = false;

    public HiddenEvent2(GoosePatrol instance) {
        this.instance = instance;
    }

    /** sets goose target to null so they will stop following */
    public void fire() {
        List<Entity> entities = instance.getEntityManager().getEntities();

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            if (entity instanceof Goose) {
                Vector2 Goose_Position = new Vector2(entity.getPosX(), entity.getPosY());
                Vector2 Player_Position = new Vector2(instance.getEntityManager().Get_Player().getPosX(),
                        instance.getEntityManager().Get_Player().getPosY());
                if (Goose_Position.dst(Player_Position) < 200f) {
                    entity.Set_To_Follow(null);
                }
            }
        }
    }

    /** Spawns 5 geese within the lake and marks the event as triggered */
    public void activate() {
        if (hasTriggeed)
            return;
        Random randNum = new Random();
        for (int i = 0; i < 5; i++) {
            Goose goose = new Goose(randNum.nextFloat(850f, 950f),
                    randNum.nextFloat(650f, 750f), "goose/single texture/goosee.png", true, true, this.instance);
            goose.Set_To_Follow(instance.getEntityManager().Get_Player());
            this.instance.getEntityManager().addEntity(goose);
        }
        hasTriggeed = true;

    }

    // getter for the geese to read
    public boolean hasTriggered() {
        return hasTriggeed;
    }

}
