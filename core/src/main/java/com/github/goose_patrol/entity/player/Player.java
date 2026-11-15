package com.github.goose_patrol.entity.player;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.*;

/**
 * Class for the player entity
 * <p>
 * Defines it's size, speed, assets for animation and inventory
 * <p>
 * Ticks every frame in which movement, animation and use of inventory items are
 * handled
 */
public class Player extends Entity {

    /**
     * Size of the player's body in world units (placeholder: same 6:1 shape as
     * libgdx.png).
     */
    public static final float WIDTH = 50f;
    public static final float HEIGHT = 60f;
    /** Walking speed in world units per second. */
    public static float SPEED = 1000f;

    /** Map of direction states to their corresponding textures */
    EnumMap<directionState, TextureRegion> directionIdleTextures = new EnumMap<>(directionState.class);
    /** Map of walking animation sheets to their corresponding direction states */
    EnumMap<directionState, Animation<TextureRegion>> walkingAnimations = new EnumMap<>(directionState.class);

    private enum actionState {
        BREAD_THROW, PHONE_USE
    }

    private actionState currentAction = null; // Track the current action state for animation purposes
    /**
     * Map of possible action states {BREAD_THROW, PHONE_USE} to their corresponding
     * animation sheets (Cardinal Directions only)
     */
    EnumMap<actionState, EnumMap<directionState, Animation<TextureRegion>>> actionAnimations = new EnumMap<>(
            actionState.class);

    private float stateTime = 0f; // Time spent in current state, for future animation purposes.
    private final Array<Texture> textures = new Array<>(); // Keep track of loaded textures for disposal

    private InventoryItem[] inventory;
    private int heldItemNumber;
    private int Number_Of_Items_Used = 0;

    public Player(float posX, float posY, InventoryItem[] initialInventory, String image, GoosePatrol instance) {

        super(posX, posY, image, new BoundingBox(posX, posX + WIDTH, posY, posY + HEIGHT), instance);
        this.inventory = initialInventory;
        this.heldItemNumber = 0;

        // Load textures now to avoid doing it repeatedly later. This is a temporary
        // solution until we implement an AssetManager.
        loadTextures();

    }

    /**
     * On press of WASD / arrow keys, moves the player; Pressing keys 1, 2, 3, or 4
     * selects the corresponding item slot, the item event is triggered by pressing
     * the ENTER key.
     * 
     * @param delta the time elapsed since the last frame
     */
    @Override
    public void tickEntity(float delta) {

        float dirX = 0;
        float dirY = 0;
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
            dirX -= 1;
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
            dirX += 1;
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
            dirY -= 1;
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
            dirY += 1;

        // check if the player was moving before this tick, to determine if we need to
        // reset the state time (animation)
        boolean wasMoving = this.isMoving;
        float oldPosX = posX, oldPosY = posY; // Store old position
        moveEntity(dirX, dirY, delta, this.instance.getRenderManager().getMaxX(),
                this.instance.getRenderManager().getMaxY());

        // Check for inventory slot key presses (1-4)
        if (Gdx.input.isKeyPressed(Keys.NUM_1))
            heldItemNumber = 0;
        if (Gdx.input.isKeyPressed(Keys.NUM_2))
            heldItemNumber = 1;
        if (Gdx.input.isKeyPressed(Keys.NUM_3))
            heldItemNumber = 2;
        if (Gdx.input.isKeyPressed(Keys.NUM_4))
            heldItemNumber = 3;
        if (Gdx.input.isKeyPressed(Keys.ENTER) && heldItemNumber >= 0 && inventory != null
                && inventory[heldItemNumber] != null && inventory[heldItemNumber].getUseEvent().isPresent()) {
            inventory[heldItemNumber].useItem(); // use the corresponding item if available
            inventory[heldItemNumber] = null; // Remove the item from the inventory after use
            Number_Of_Items_Used++; // Increment the number of items used
        }

        // we need to update the current direction state based on movement
        // 1 Check if the player is moving in any direction, by change from old position
        // to new position
        this.isMoving = posX != oldPosX || posY != oldPosY;

        // 2 Update the player's state based on movement direction
        if (dirX != 0 || dirY != 0) {
            // determine the direction based on deltaX and deltaY
            this.setCurrentDirection(getDirectionState(dirX, dirY));
        }
        // else doesnt matter, no change from last direction if not moving,

        // 3 Update the state time for animation purposes, flip if player stopped moving
        if (isMoving != wasMoving) {
            stateTime = 0f; // reset state time if movement state changed
        } else {
            stateTime += delta; // increment state time if still moving
        }

        super.tickEntity(delta);
    }

    /**
     * Moves the player according to the current input and handles collisions
     * 
     * @param deltaX    the change in x position
     * @param deltaY    the change in y position
     * @param delta     the time elapsed since the last frame
     * @param mapWidth  the width of the map
     * @param mapHeight the height of the map
     */
    public void moveEntity(float deltaX, float deltaY, float delta, float mapWidth, float mapHeight) {

        Vector2 direction = new Vector2(deltaX, deltaY);
        if (!direction.isZero()) {
            direction.nor(); // stops diagonal movement being faster
        }
        Vector2 position = new Vector2();
        position.x = posX;
        position.y = posY;
        Area currentArea = MapHelper.getCurrentArea(position.x, position.y);
        if (currentArea == Area.GRASS)
            SPEED = 100f;
        else
            SPEED = 200f;
        position.mulAdd(direction, SPEED * delta);
        position.x = MathUtils.clamp(position.x, 0, mapWidth - WIDTH);
        position.y = MathUtils.clamp(position.y, 0, mapHeight - HEIGHT);

        // Handles entity collisions
        BoundingBox newBoundingBox = getEntityBoundingBox().clone();
        newBoundingBox.setMinX(newBoundingBox.getMinX() + deltaX);
        newBoundingBox.setMinY(newBoundingBox.getMinY() + deltaY);
        newBoundingBox.setMaxX(newBoundingBox.getMaxX() + deltaX);
        newBoundingBox.setMaxY(newBoundingBox.getMaxY() + deltaY);
        if (MapHelper.isColliding(instance.getEntityManager(), newBoundingBox, this) == 1) {

            position.x = posX;
            position.y = posY;

        }

        if (MapHelper.isColliding(instance.getEntityManager(), newBoundingBox, this) == 2) {

            instance.getRenderManager().refreshButtons();
            instance.STATE = State.WIN;
            return;

        }

        Area moveIntoArea = MapHelper.getCurrentArea(position.x, position.y);
        if (moveIntoArea == Area.LAKE) {

            position.x = posX;
            position.y = posY;

        }

        this.posX = position.x;
        this.posY = position.y;

        this.getEntityBoundingBox().setMinX(this.posX);
        this.getEntityBoundingBox().setMaxX(this.posX + WIDTH);
        this.getEntityBoundingBox().setMinY(this.posY);
        this.getEntityBoundingBox().setMaxY(this.posY + HEIGHT);

    }

    /**
     * Load texture or animation based on the current direction state and movement
     * status of the player
     * 
     * @return an animation if the player is momving, an dile texture otherwise
     */
    @Override
    public TextureRegion getEntityTexture() {
        directionState dir = getCurrentDirection();
        if (isMoving) {
            Animation<TextureRegion> anim = walkingAnimations.get(dir);
            if (anim != null)
                return anim.getKeyFrame(stateTime, true);
        } else if (currentAction != null) {
            // animate the action if the player is performing an action (e.g., throwing
            // bread or using phone)
            Animation<TextureRegion> anim = actionAnimations.get(currentAction).get(dir);
            if (anim != null && anim.isAnimationFinished(stateTime)) {
                // If the action animation is finished, reset state time and return idle texture
                stateTime = 0f;
                return directionIdleTextures.get(dir);
            } else if (anim != null) {
                // If the action animation is still playing, return the current frame
                return anim.getKeyFrame(stateTime, false);
            }
        }
        return directionIdleTextures.get(dir);
    }

    /**
     * Start the animation for a specific action given the action state
     * {BREAD_THROW, PHONE_USE}
     */
    public void startActionAnimation(actionState action) {
        directionState dir = toCardinal(getCurrentDirection());
        Animation<TextureRegion> anim = actionAnimations.get(action).get(dir);
        if (anim != null) {
            currentAction = action;
            stateTime = 0f; // reset state time for the action animation
            isMoving = false; // stop walking animation while performing action
        }
    }

    /**
     * Load textures for player and each direction state. Assets stored at
     * 'eng1-c3-g10\assets\Player'
     */
    private void loadTextures() {
        // Should be handled by AssetManager in future but for now we can just load them
        // directly
        final int frameW = 32, frameH = 32, frameCount = 5;

        for (directionState d : directionState.values()) {
            String texturePath = "player/Player" + d.name() + "_spritesheet.png";
            Texture sheet = new Texture(Gdx.files.internal(texturePath));
            sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textures.add(sheet); // keep a list so you can dispose them later
            // textures must be disposed of when the game ends to free up memory, this is a
            // temporary solution until AssetManager implementation.

            // Frame 0 = idle
            directionIdleTextures.put(d, new TextureRegion(sheet, 0, 0, frameW, frameH));

            // Frames 1..4 = walk cycle (use i = 0 if idle is part of the walk too)
            Array<TextureRegion> frames = new Array<>();
            for (int i = 1; i < frameCount; i++) {
                frames.add(new TextureRegion(sheet, i * frameW, 0, frameW, frameH));
            }

            walkingAnimations.put(d, new Animation<>(0.1f, frames, Animation.PlayMode.LOOP));

            // Load the assets for action animations (EACH action has its own spritesheet
            // for each cardinal direction only)
            if (d == directionState.N || d == directionState.E || d == directionState.S || d == directionState.W) {
                // These are 5 frame animations, with the first frame being the idle frame (same
                // as the idle texture for that direction)

                // Bread throw animation first
                String actionTexturePath = "player/Actions/Player" + d.name() + "_throw_spritesheet.png";
                Texture actionSheet = new Texture(Gdx.files.internal(actionTexturePath));
                actionSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                textures.add(actionSheet); // add to textures list so you can dispose them later
                // reuse the frames array to store the frames for this action animation
                frames.clear();
                for (int i = 1; i < 4; i++) {
                    frames.add(new TextureRegion(actionSheet, i * frameW, 0, frameW, frameH));
                }
                // since it is not yet initialised, we need to create the inner map on demand
                // using the computeIfAbsent method.
                // Animation.PlayMode.NORMAL means the animation will play once and then stop at
                // the last frame
                actionAnimations
                        .computeIfAbsent(actionState.BREAD_THROW, k -> new EnumMap<>(directionState.class))
                        .put(d, new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL));

                // Phone use animation next
                actionTexturePath = "player/Actions/Player" + d.name() + "_phone_spritesheet.png";
                actionSheet = new Texture(Gdx.files.internal(actionTexturePath));
                actionSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                textures.add(actionSheet); // add to textures list so you can dispose them later
                // reuse the frames array to store the frames for this action animation
                frames.clear();
                for (int i = 1; i < 4; i++) {
                    frames.add(new TextureRegion(actionSheet, i * frameW, 0, frameW, frameH));
                }
                // since it is not yet initialised, we need to create the inner map on demand
                // using the computeIfAbsent method.
                // Animation.PlayMode.NORMAL means the animation will play once and then stop at
                // the last frame
                actionAnimations
                        .computeIfAbsent(actionState.PHONE_USE, k -> new EnumMap<>(directionState.class))
                        .put(d, new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL));
            }
        }

    }

    /**
     * Helper method to determine the direction that the player is facing/moving
     * into
     */
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

    /**
     * Helper method to convert diagonal direction states to cardinal direction
     * states
     */
    private directionState toCardinal(directionState d) {
        switch (d) {
            case NE:
            case SE:
                return directionState.E;
            case NW:
            case SW:
                return directionState.W;
            default:
                return d; // N, E, S, W pass straight through
        }
    }

    /**
     * Disposes of all loaded Player textures to free up memory. (for current
     * insanse only)
     */
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }

    public float getCenterX() {
        return posX + WIDTH / 2;
    }

    public int getHeldItem() {

        return this.heldItemNumber;

    }

    public float getCenterY() {
        return posY + HEIGHT / 2;
    }

    public InventoryItem[] getInventory() {
        return inventory;
    }

    public int Get_Number_Of_Items_Used() {
        return Number_Of_Items_Used;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

}
