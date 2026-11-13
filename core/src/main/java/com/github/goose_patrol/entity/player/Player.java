package com.github.goose_patrol.entity.player;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.Area;
import com.github.goose_patrol.util.BoundingBox;
import com.github.goose_patrol.util.InventoryItem;
import com.github.goose_patrol.util.MapHelper;

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
    EnumMap<directionState, Texture> directionTextures = new EnumMap<>(directionState.class);

    private InventoryItem[] inventory;
    private int heldItemNumber;

    public Player(float posX, float posY, InventoryItem[] initialInventory, String image, GoosePatrol instance) {

        super(posX, posY, image, true, new BoundingBox(posX, posX + WIDTH, posY, posY + HEIGHT), instance);
        this.inventory = initialInventory;
        this.heldItemNumber = 0;

        // Load textures now to avoid doing it repeatedly later. This is a temporary
        // solution until we implement an AssetManager.
        loadTextures();

    }

    // On press of WASD / arrow keys, moves the player; Pressing keys 1, 2, 3, or 4 selects the corresponding item slot, the item event is triggered by pressing the ENTER key.
    @Override
    public void tickEntity(float delta) {


        float dirX = 0;
        float dirY = 0;
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) dirX -= 1;
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) dirX += 1;
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) dirY -= 1;
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) dirY += 1;
        moveEntity(dirX, dirY, delta, this.instance.getRenderManager().getMaxX(), this.instance.getRenderManager().getMaxY());

        // Check for inventory slot key presses (1-4)
        if (Gdx.input.isKeyPressed(Keys.NUM_1)) heldItemNumber = 0;
        if (Gdx.input.isKeyPressed(Keys.NUM_2)) heldItemNumber = 1;
        if (Gdx.input.isKeyPressed(Keys.NUM_3)) heldItemNumber = 2;
        if (Gdx.input.isKeyPressed(Keys.NUM_4)) heldItemNumber = 3;
        if (Gdx.input.isKeyPressed(Keys.ENTER) && heldItemNumber >=0 && inventory != null && inventory[heldItemNumber] != null && inventory[heldItemNumber].getUseEvent().isPresent())
        {
            inventory[heldItemNumber].useItem(); //use the corresponding item if available
            inventory[heldItemNumber] = null; // Remove the item from the inventory after use
        }
        super.tickEntity(delta);

    }

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
        if (MapHelper.isColliding(instance.getEntityManager(), newBoundingBox, this)) {

            position.x = posX;
            position.y = posY;

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

        System.out.println("X: " + posX);
        System.out.println("Y: " + posY);

        // Update the current direction state based on movement
        // Check if the player is moving in any direction
        boolean isMoving = Math.abs(deltaX) > 0 || Math.abs(deltaY) > 0;

        // Update the player's state based on movement direction
        if (isMoving) {
            // player is moving, determine the direction based on deltaX and deltaY
            this.setCurrentDirection(getDirectionState(deltaX, deltaY));
        } else {
            this.setCurrentDirection(directionState.S); // player is idle, set to south-facing direction
        }

    }

    // Load texture based on the current direction state of the player
    @Override
    public Texture getEntityTexture() {
        // Return the texture corresponding to the current direction state
        return directionTextures.get(getCurrentDirection());
    }

    private void loadTextures() {
        // Load textures for each direction state
        // assets stored at 'eng1-c3-g10\assets\Player'
        // Should be handled by AssetManager in future but for now we can just load them
        // directly
        for (directionState d : directionState.values()) {
            String texturePath = "player/Player" + d.name() + ".png";
            Texture texture = new Texture(Gdx.files.internal(texturePath));
            directionTextures.put(d, texture);
        }
    }

    // Helper method to determine the direction state
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

}
