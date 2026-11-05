package com.github.goose_patrol;

/**
   The area of the world the player can walk on.
   Currently a plain rectangle with its bottom-left corner at (0, 0); it is kept
   separate from rendering so it can be replaced by a campus map later.
 */
public class GameMap {
    private final float width;
    private final float height;

    /**
     * @param width  width of the walkable area in world units
     * @param height height of the walkable area in world units
     */
    public GameMap(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}