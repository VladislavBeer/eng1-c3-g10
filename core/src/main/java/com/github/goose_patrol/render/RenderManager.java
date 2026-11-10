package com.github.goose_patrol.render;

import com.github.goose_patrol.entity.player.Player;

public class RenderManager {

    private String backgroundFile;
    private final float maxX;
    private final float maxY;
    private RenderHandler renderHandler;

    public RenderManager(float width, float height, Player player) {
        this.maxX = width;
        this.maxY = height;
        this.renderHandler = new RenderHandler(player, this);
    }

    //TODO
    public void renderMap() {
        
    }

    //TODO
    public void renderPlayerOverlay() {

    }

    //TODO
    public void renderEntities() {

    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public RenderHandler getRenderHandler() {
        return this.renderHandler;
    }
    

}
