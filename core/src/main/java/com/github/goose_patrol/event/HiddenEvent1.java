package com.github.goose_patrol.event;

import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.entity.Goose;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.BoundingBox;
import com.github.goose_patrol.util.Event;
import java.util.List;
//import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * The first hidden event, triggered on the bottom right corner of the map,
 * which will spawn 5 geese and must be beaten using the bread item
 * <p>
 * Contains methods that add geese to the entity manager when activated and
 * changes the geese's behaviour when the bread item is used
 */
public class HiddenEvent1 implements Event {

    private GoosePatrol instance;
    private boolean hasTriggeed = false;

    public HiddenEvent1(GoosePatrol instance) {
        this.instance = instance;
    }

    /**
     * Creates new bread entity in the lake and reassigns goose pathfinding target
     * to bread
     */
    public void fire() {
        Entity Bread_Entity = new Entity(1100f, 400f, "Items/bread.png", new BoundingBox(1100f, 1125f, 400f, 425f),
                instance);
        this.instance.getRenderManager().getRenderHandler().addTickTimeEntity(Bread_Entity);

        List<Entity> entities = instance.getEntityManager().getEntities();

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            if (entity instanceof Goose) {
                // Vector2 Goose_Position = new Vector2(entity.getPosX(), entity.getPosY());
                // Vector2 Player_Position = new
                // Vector2(instance.getEntityManager().Get_Player().getPosX(),
                // instance.getEntityManager().Get_Player().getPosY());
                // if (Goose_Position.dst(Player_Position) < 200f) {
                entity.Set_To_Follow(Bread_Entity);
                // }
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
                    randNum.nextFloat(450f, 550f), "goose/single texture/goosee.png", true, true, this.instance);
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
