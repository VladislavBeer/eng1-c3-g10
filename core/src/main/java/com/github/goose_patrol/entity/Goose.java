package com.github.goose_patrol.entity;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.BoundingBox;

public class Goose extends Entity {

    private boolean triggered;

    /** Map of direction states to their corresponding textures */
    EnumMap<directionState, Texture> directionTextures = new EnumMap<>(directionState.class);

    public Goose(float posX, float posY, String image, boolean active, boolean triggered, GoosePatrol instance) {
        super(posX, posY, image, active, new BoundingBox(posX, posX + 50, posY, posY + 50), instance);
        this.triggered = triggered;

        // Load textures now to avoid doing it repeatedly later.
        loadTextures();
        this.setCurrentDirection(directionState.E); // default to east-facing
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
    
    // Load texture based on the current direction state of the goose
    @Override
    public Texture getEntityTexture() {
        // Return the texture corresponding to the current direction state
        return directionTextures.get(getCurrentDirection());
    }

    private void loadTextures() {
        // Load textures for each direction state
        // assets stored at 'eng1-c3-g10\assets\Goose'
        // Should be handled by AssetManager in future but for now we can just load them directly
        for (directionState d : directionState.values()) {
            String texturePath = "goose/Goose" + d.name() + ".png";
            Texture texture = new Texture(Gdx.files.internal(texturePath)); 
            directionTextures.put(d, texture);
        }
    }

    // Helper method to determine the direction state, ported from Player.java may need to be modified for goose movement
    private directionState getDirectionState(float deltaX, float deltaY) {
        // Determine the sign of deltaX and deltaY to find the direction
        // Works as positive deltaY is north.
        int signX = deltaX > 0 ? 1 : (deltaX < 0 ? -1 : 0);
        int signY = deltaY > 0 ? 1 : (deltaY < 0 ? -1 : 0);

        if (signX == 0 && signY > 0)
            return directionState.N;
        if (signX > 0 && signY > 0)
            return directionState.NE;
        if (signX < 0 && signY > 0)
            return directionState.NW;
        if (signX == 0 && signY < 0)
            return directionState.S;
        if (signX > 0 && signY < 0)
            return directionState.SE;
        if (signX < 0 && signY < 0)
            return directionState.SW;
        if (signX > 0 && signY == 0)
            return directionState.E;
        if (signX < 0 && signY == 0)
            return directionState.W;

        return directionState.S; // default to south-facing if no movement
    }

}
