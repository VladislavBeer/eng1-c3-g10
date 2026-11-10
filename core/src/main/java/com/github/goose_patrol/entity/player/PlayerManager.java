package com.github.goose_patrol.entity.player;

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
