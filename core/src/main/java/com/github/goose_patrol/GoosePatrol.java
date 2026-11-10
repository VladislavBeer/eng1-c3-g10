package com.github.goose_patrol;

import com.badlogic.gdx.Game;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.entity.player.PlayerManager;
import com.github.goose_patrol.render.RenderManager;

/** Entry point shared by all platforms. Starts the game on the {@link GameScreen}. */
public class GoosePatrol extends Game {

    private RenderManager renderManager;
    private PlayerManager playerManager;

    @Override
    public void create() {
        final float mapWidth = 1600f;
        final float mapHeight = 1200f;
        Player player = new Player(mapWidth / 2 - Player.WIDTH / 2, mapHeight / 2 - Player.HEIGHT / 2, null, "libgdx.png");
        this.playerManager = new PlayerManager(player);
        this.renderManager = new RenderManager(1600f, 1200f, player);
        setScreen(renderManager.getRenderHandler());
    }

    @Override
    public void dispose() {
        // Game.dispose() only hides the current screen, so dispose of it explicitly.
        if (getScreen() != null) {
            getScreen().dispose();
        }
        super.dispose();
    }
}