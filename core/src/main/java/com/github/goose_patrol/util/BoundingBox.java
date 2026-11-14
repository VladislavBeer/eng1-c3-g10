package com.github.goose_patrol.util;

/**
 * Class for bounding boxes, used for detecting collision between entities
 * <p>
 * Have a min and max x and y coordinate, which outlines a square/rectangle for
 * collision
 */
public class BoundingBox {

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public BoundingBox(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public BoundingBox setMinX(float minX) {
        this.minX = minX;
        return this;
    }

    public BoundingBox setMaxX(float maxX) {
        this.maxX = maxX;
        return this;
    }

    public BoundingBox setMinY(float minY) {
        this.minY = minY;
        return this;
    }

    public BoundingBox setMaxY(float maxY) {
        this.maxY = maxY;
        return this;
    }

    public BoundingBox clone() {

        return new BoundingBox(minX, maxX, minY, maxY);

    }

}
