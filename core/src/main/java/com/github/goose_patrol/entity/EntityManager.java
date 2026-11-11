package com.github.goose_patrol.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.goose_patrol.GoosePatrol;

public class EntityManager {

    private GoosePatrol instance;
    private List<Entity> loadedEntities;

    public EntityManager(GoosePatrol instance) {
        this.instance = instance;
        this.loadedEntities = new ArrayList<>();
    }

    public void tickEntities(float delta) {
        loadedEntities.forEach(e -> e.tickEntity(delta));
    }

    public void addEntity(Entity e) {
        loadedEntities.add(e);
    }

    public List<Entity> getEntities() {
        return this.loadedEntities;
    }

}
