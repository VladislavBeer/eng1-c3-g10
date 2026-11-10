package com.github.goose_patrol.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.goose_patrol.entity.player.Player;

// Main gameplay screen: reads input, updates the player and draws the map and player.
public class RenderHandler extends ScreenAdapter {
    private static final float VIEW_WIDTH = 640f;
    private static final float VIEW_HEIGHT = 480f;
    // Spacing of the placeholder grid lines that make scrolling visible. Take out once we have a proper map.
    private static final float GRID_SPACING = 100f;

    private final Player player;
    private final RenderManager renderManager;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final FitViewport viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();

    public RenderHandler(Player player, RenderManager manager) {
        this.player = player;
        this.renderManager = manager;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    // On press of WASD / arrow keys, moves the player
    private void update(float delta) {
        float dirX = 0;
        float dirY = 0;
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) dirX -= 1;
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) dirX += 1;
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) dirY -= 1;
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) dirY += 1;
        player.moveEntity(dirX, dirY, delta, renderManager.getMaxX(), renderManager.getMaxY());

        // Keep the player centred but if they try walk past the edge, reset their position before the render is updated.
        float halfWidth = viewport.getWorldWidth() / 2;
        float halfHeight = viewport.getWorldHeight() / 2;
        camera.position.x = MathUtils.clamp(player.getCenterX(), halfWidth, renderManager.getMaxX() - halfWidth);
        camera.position.y = MathUtils.clamp(player.getCenterY(), halfHeight, renderManager.getMaxY() - halfHeight);
        camera.update();
    }

    // Function to render the game screen, including the map, grid lines, and player.
    private void draw() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
        viewport.apply();
        shapes.setProjectionMatrix(camera.combined); // turns world coords into screen positions
        shapes.begin(ShapeType.Filled);
        // Walkable area (placeholder)
        shapes.setColor(0.2f, 0.6f, 0.2f, 1f);
        shapes.rect(0, 0, renderManager.getMaxX(), renderManager.getMaxY());
        shapes.end();

        // Grid lines (placeholder scenery) so movement is visible
        shapes.begin(ShapeType.Line);
        shapes.setColor(0.15f, 0.45f, 0.15f, 1f);
        for (float x = 0; x <= renderManager.getMaxX(); x += GRID_SPACING) {
            shapes.line(x, 0, x, renderManager.getMaxY());
        }
        for (float y = 0; y <= renderManager.getMaxY(); y += GRID_SPACING) {
            shapes.line(0, y, renderManager.getMaxX(), y);
        }
        shapes.end();

        // Player
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(player.getEntityTexture(), player.getPosX(), player.getPosY(), (player.getEntityBoundingBox().getMaxX() - player.getEntityBoundingBox().getMinX()), (player.getEntityBoundingBox().getMaxY() - player.getEntityBoundingBox().getMinY()));
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        player.getEntityTexture().dispose();
    }
}