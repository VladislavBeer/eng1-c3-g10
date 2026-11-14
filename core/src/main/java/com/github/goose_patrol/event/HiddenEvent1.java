package com.github.goose_patrol.event;

import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.entity.Goose;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.BoundingBox;
import com.github.goose_patrol.util.Event;
import java.util.List;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class HiddenEvent1 implements Event {

    private GoosePatrol instance;
    private boolean hasTriggeed = false;

    public HiddenEvent1(GoosePatrol instance) {
        this.instance = instance;
    }

    public void fire() {
        Entity Bread_Entity = new Entity(400f, 450f, "Items/bread.png", new BoundingBox(400f, 425f, 450f, 475f),instance);
        this.instance.getRenderManager().getRenderHandler().addTickTimeEntity(Bread_Entity);
        
        List<Entity> entities = instance.getEntityManager().getEntities();

        for (int i = 0; i < entities.size(); i++) 
        {
            Entity entity = entities.get(i);

            if (entity instanceof Goose)
            {
                Vector2 Goose_Position = new Vector2(entity.getPosX(), entity.getPosY());
                Vector2 Player_Position = new Vector2(instance.getEntityManager().Get_Player().getPosX(), instance.getEntityManager().Get_Player().getPosY());
                if (Goose_Position.dst(Player_Position) < 200f)
                {
                    entity.Set_To_Follow(Bread_Entity);
                }
            }
        } 
    }

    public void activate() {
        if (hasTriggeed)
            return;
        Random randNum = new Random();
        for (int i = 0; i < 5; i++) {
            Goose goose = new Goose(randNum.nextFloat(850f, 950f),
                    randNum.nextFloat(450f, 550f), "goose/goosee.png", true, true, this.instance);
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
