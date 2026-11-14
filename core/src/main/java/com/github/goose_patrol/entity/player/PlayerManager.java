package com.github.goose_patrol.entity.player;

/**
 * Manager for player class, for use of other classes to access player methods
 */
public class PlayerManager {

    private Player playerEntity;

    public PlayerManager(Player player) {
        this.playerEntity = player;
    }

    public void tickPlayer() {

    }

    public Player getPlayer() {
        return this.playerEntity;
    }

}
