package com.github.goose_patrol.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.util.BoundingBox;
import com.github.goose_patrol.util.InventoryItem;

public class Player extends Entity {


    /** Size of the player's body in world units (placeholder: same 6:1 shape as libgdx.png). */
    public static final float WIDTH = 50f;
    public static final float HEIGHT = 60f;
    /** Walking speed in world units per second. */
    public static final float SPEED = 200f;

    private InventoryItem[] inventory;
    private int heldItemNumber;

    public Player(float posX, float posY, InventoryItem[] initialInventory, String image, GoosePatrol instance) {

        super(posX, posY, image, true, new BoundingBox(posX, posX + WIDTH, posY, posY + HEIGHT), instance);
        this.inventory = initialInventory;
        this.heldItemNumber = 0;

    }

    // TODO
    @Override
    public void tickEntity(float delta) {


        float dirX = 0;
        float dirY = 0;
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) dirX -= 1;
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) dirX += 1;
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) dirY -= 1;
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) dirY += 1;
        moveEntity(dirX, dirY, delta, this.instance.getRenderManager().getMaxX(), this.instance.getRenderManager().getMaxY());
        super.tickEntity(delta);

    }

    //TODO
    public void moveEntity(float deltaX, float deltaY, float delta, float mapWidth, float mapHeight) {


        Vector2 direction = new Vector2(deltaX, deltaY);
        if (!direction.isZero()) {
            direction.nor(); // stops diagonal movement being faster
        }
        Vector2 position = new Vector2();
        position.x = posX;
        position.y = posY;
        position.mulAdd(direction, SPEED * delta);
        position.x = MathUtils.clamp(position.x, 0, mapWidth - WIDTH);
        position.y = MathUtils.clamp(position.y, 0, mapHeight - HEIGHT);

        this.posX = position.x;
        this.posY = position.y;

        this.getEntityBoundingBox().setMinX(this.posX);
        this.getEntityBoundingBox().setMaxX(this.posX + WIDTH);
        this.getEntityBoundingBox().setMinY(this.posY);
        this.getEntityBoundingBox().setMaxY(this.posY + HEIGHT);


    }

    public float getCenterX() {
        return posX + WIDTH / 2;
    }

    public float getCenterY() {
        return posY + HEIGHT / 2;
    }


}
