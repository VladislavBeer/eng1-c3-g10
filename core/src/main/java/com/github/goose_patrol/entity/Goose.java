package com.github.goose_patrol.entity;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.util.BoundingBox;

public class Goose extends Entity {

    private boolean triggered;

    /** Map of direction states to their corresponding textures */
    EnumMap<directionState, TextureRegion> directionIdleTextures = new EnumMap<>(directionState.class);
    /** Map of walking animation sheets to their corresponding direction states */
    EnumMap<directionState, Animation<TextureRegion>> walkingAnimations = new EnumMap<>(directionState.class);
    private final Array<Texture> textures = new Array<>(); // Keep track of loaded textures for disposal

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
    public TextureRegion getEntityTexture() {
        // Return the texture corresponding to the current direction state
        return directionIdleTextures.get(getCurrentDirection());
    }

    // Helper method to load the textures, ported from Player.java but modified for goose.
    private void loadTextures() {
        // Load textures for each direction state
        // assets stored at 'eng1-c3-g10\assets\Goose'
        // Should be handled by AssetManager in future but for now we can just load them directly
        final int frameW = 32, frameH = 32, frameCount = 5;

        for (directionState d : directionState.values()) {
            String texturePath = "goose/Goose" + d.name() + "_spritesheet.png";
            Texture sheet = new Texture(Gdx.files.internal(texturePath));
            sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textures.add(sheet); // keep a list so you can dispose them later
            // textures must be disposed of when the game ends to free up memory, this is a temporary solution until implement an AssetManager implementation.

            // Frame 0 = idle
            directionIdleTextures.put(d, new TextureRegion(sheet, 0, 0, frameW, frameH));

            // Frames 1..4 = walk cycle (use i = 0 if idle is part of the walk too)
            Array<TextureRegion> frames = new Array<>();
            for (int i = 1; i < frameCount; i++) {
                frames.add(new TextureRegion(sheet, i * frameW, 0, frameW, frameH));
            }

            walkingAnimations.put(d, new Animation<>(0.1f, frames, Animation.PlayMode.LOOP));
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

    public void disposeTextures() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }
}
