package com.github.goose_patrol;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.github.goose_patrol.entity.EntityManager;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.entity.player.PlayerManager;
import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.InventoryItem;
import com.github.goose_patrol.util.State;

/** Entry point shared by all platforms. Starts the game on the {@link com.github.goose_patrol.render.RenderHandler}. */
public class GoosePatrol extends Game {

    private RenderManager renderManager;
    private PlayerManager playerManager;
    private EntityManager entityManager;
    // private AssetManager assetManager; for future use if we want to load assets asynchronously
    public State STATE;

    @Override
    public void create() {
        init();
    }

    @Override
    public void dispose() {
        // Game.dispose() only hides the current screen, so dispose of it explicitly.
        if (getScreen() != null) {
            getScreen().dispose();
        }
        super.dispose();
    }

    public void init() {

        final float mapWidth = 1600f;
        final float mapHeight = 1200f;
        Player player = new Player(195, 224, new InventoryItem[]{new InventoryItem("Bread","Items/bread.png"),new InventoryItem("Phone","Items/music_phone.png"),null,null}, "player/playere.png", this);
        this.playerManager = new PlayerManager(player);
        this.entityManager = new EntityManager(this);
        this.entityManager.addEntity(player);
        this.renderManager = new RenderManager(this, 1600f, 1200f, player);
        this.STATE = State.PRE;
        setScreen(renderManager.getRenderHandler());
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

}
