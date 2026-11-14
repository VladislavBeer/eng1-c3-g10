package com.github.goose_patrol.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.BoundingBox;

public class Entity {


    protected float posX;
    protected float posY;
    private String image;
    private BoundingBox entityBoundingBox;
    private final TextureRegion entityTexture;
    protected GoosePatrol instance;

    // enum used to track direction of movement, for animation purposes.
    protected enum directionState {
        W, E, NW, N, NE, SW, S, SE
    }
    private directionState currentDirection;
    private float stateTime; // Time spent in current state, for future animation purposes.


    public Entity(float posX, float posY, String image, BoundingBox entityBoundingBox, GoosePatrol instance) {

        this.posX = posX;
        this.posY = posY;
        this.image = image;
        this.instance = instance;
        this.entityBoundingBox = entityBoundingBox;
        this.entityTexture = new TextureRegion(new Texture(Gdx.files.internal(image)));
        // debug
        // System.out.println("Entity ctor: " + getClass().getSimpleName() + " image=" + image + " region=" + entityTexture.toString());
    }

    public boolean isStatic() {
        return false; // default to false, override in subclasses
    }

    public void tickEntity(float delta) {

    }

    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public BoundingBox getEntityBoundingBox() {
        return entityBoundingBox;
    }


    public TextureRegion getEntityTexture() {
        return this.entityTexture;
    }


    // additional getters and setters for tracking direction and state time, for animation purposes
    public directionState getCurrentDirection() {
        return currentDirection;
    }
    public void setCurrentDirection(directionState newDirection) {
        this.currentDirection = newDirection;
    }
    public float getStateTime() {
        return stateTime;
    }

    public void Set_X(float newX) {
        this.posX = newX;
    }
    
    public void Set_Y(float newY) {
        this.posY = newY;
    }

    public void Set_To_Follow(Entity entity) {}
}
