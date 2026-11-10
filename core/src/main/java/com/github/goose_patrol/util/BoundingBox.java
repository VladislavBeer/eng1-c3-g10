package com.github.goose_patrol.util;

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

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    

}
