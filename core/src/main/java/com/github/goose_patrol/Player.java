package com.github.goose_patrol;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** The student character controlled by the player. Holds position and movement logic only. */
public class Player {
    /** Size of the player's body in world units (placeholder: same 6:1 shape as libgdx.png). */
    public static final float WIDTH = 192f;
    public static final float HEIGHT = 32f;
    /** Walking speed in world units per second. */
    public static final float SPEED = 200f;

    /** Bottom-left corner of the player. */
    private final Vector2 position;

    /**
     * @param x starting x position (bottom-left corner)
     * @param y starting y position (bottom-left corner)
     */
    public Player(float x, float y) {
        position = new Vector2(x, y);
    }

    /**
     * Moves the player and keeps them inside the map.
     *
     * @param dirX  horizontal direction, -1 (left) to 1 (right)
     * @param dirY  vertical direction, -1 (down) to 1 (up)
     * @param delta seconds since the last update
     * @param map   map whose limits the player cannot leave
     */
    public void move(float dirX, float dirY, float delta, GameMap map) {
        Vector2 direction = new Vector2(dirX, dirY);
        if (!direction.isZero()) {
            direction.nor(); // stops diagonal movement being faster
        }
        position.mulAdd(direction, SPEED * delta);
        position.x = MathUtils.clamp(position.x, 0, map.getWidth() - WIDTH);
        position.y = MathUtils.clamp(position.y, 0, map.getHeight() - HEIGHT);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getCenterX() {
        return position.x + WIDTH / 2;
    }

    public float getCenterY() {
        return position.y + HEIGHT / 2;
    }
}