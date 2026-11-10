package com.github.goose_patrol.entity;

import com.badlogic.gdx.graphics.Texture;
import com.github.goose_patrol.util.BoundingBox;

public class Entity {


    protected float posX;
    protected float posY;
    private String image;
    private BoundingBox entityBoundingBox;
    private boolean active;
    private final Texture entityTexture;

    public Entity(float posX, float posY, String image, boolean active, BoundingBox entityBoundingBox) {

        this.posX = posX;
        this.posY = posY;
        this.image = image;
        this.active = active;
        this.entityBoundingBox = entityBoundingBox;
        this.entityTexture = new Texture(image);

    }

    public void tickEntity() {

    }

    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public String getImage() {
        return image;
    }
    public BoundingBox getEntityBoundingBox() {
        return entityBoundingBox;
    }
    public boolean isActive() {
        return active;
    }

    public Texture getEntityTexture() {
        return this.entityTexture;
    }
    


}
