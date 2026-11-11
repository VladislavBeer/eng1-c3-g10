package com.github.goose_patrol.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.render.button.Button;
import com.github.goose_patrol.util.HiddenEvent1;
import com.github.goose_patrol.util.HiddenEvent2;
import com.github.goose_patrol.util.State;

// Main gameplay screen: reads input, updates the player and draws the map and player.
public class RenderHandler extends ScreenAdapter {
    private static final float VIEW_WIDTH = 640f;
    private static final float VIEW_HEIGHT = 480f;
    // Spacing of the placeholder grid lines that make scrolling visible. Take out
    // once we have a proper map.
    private static final float GRID_SPACING = 100f;

    private Stage stage;

    // Timer logic
    private float timeElapsed = 0;
    private final float MAX_TIME = 300;
    private boolean gameOver = false;
    private int ticksRemaining;

    private long lastTick;

    private final BitmapFont font;
    private final Texture splashTexture;
    private final Texture pauseMenuTexture;
    private final Texture loseMenuTexture;
    private final Texture resumeTexture;
    private final Texture quitTexture;
    private final Texture mapTexture;
    private final Player player;
    private final RenderManager renderManager;
    private final HiddenEvent1 hiddenEvent1;
    private final HiddenEvent2 hiddenEvent2;
    final OrthographicCamera camera = new OrthographicCamera();
    private final FitViewport viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();

    public RenderHandler(Player player, RenderManager manager) {
        this.player = player;
        batch.setProjectionMatrix(camera.combined);
        viewport.apply();
        this.font = new BitmapFont();
        ticksRemaining = 6000;
        this.mapTexture = new Texture("static/map_base.png");
        this.splashTexture = new Texture("splash/splash.png");
        this.pauseMenuTexture = new Texture("splash/pause.png");
        this.resumeTexture = new Texture("splash/resume.png");
        this.quitTexture = new Texture("splash/quit.png");
        this.loseMenuTexture = new Texture("splash/lose.png");
        this.renderManager = manager;
        this.hiddenEvent1 = new HiddenEvent1(manager.getInstance());
        this.hiddenEvent2 = new HiddenEvent2(manager.getInstance());
    }

    Vector3 getMousePosGaame() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    Vector3 getMousePosGaame() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void render(float delta) {

        /*
         * We want to tick the game 20 times per second
         * We can use the render method to do this
         */
        if (renderManager.getInstance().STATE != State.PLAYING) {
            draw();
            checkButtonClicked();
            return;
        }

        long tickOffset = System.currentTimeMillis() - lastTick;
        if (tickOffset >= 50L) {
            // TICK
            update(delta);
            lastTick = System.currentTimeMillis();
            ticksRemaining--;
        }

        // Only update the timer if game is still active
        if (!gameOver) {

            if (timeElapsed >= MAX_TIME) {
                gameOver = true;
            }

            // ADD GAME OVER LOGIC HERE
        }

        draw();
    }

    private void checkButtonClicked() {

        float mouseX = getMousePosGaame().x;
        float mouseY = getMousePosGaame().y;
        boolean hovering = false;
        for (Button b : renderManager.getButtons()) {

            boolean hoverSpecific = b.handleClick(mouseX, mouseY, renderManager);
            if (!hovering && hoverSpecific)
                hovering = true;

        }

        if (!hovering)
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

    }

    // On press of WASD / arrow keys, moves the player
    private void update(float delta) {

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {

            this.renderManager.refreshButtons();
            renderManager.getInstance().STATE = State.PAUSE;
            return;
        }

        this.renderManager.getInstance().getEntityManager().tickEntities(delta);

        if (this.player.getPosX() >= 1200f) {
            this.hiddenEvent1.fire();
        } else if (this.player.getPosY() >= 900f) {
            this.hiddenEvent2.fire();
        }

        // Keep the player centred but if they try walk past the edge, reset their
        // position before the render is updated.
        float halfWidth = viewport.getWorldWidth() / 2;
        float halfHeight = viewport.getWorldHeight() / 2;
        camera.position.x = MathUtils.clamp(player.getCenterX(), halfWidth, renderManager.getMaxX() - halfWidth);
        camera.position.y = MathUtils.clamp(player.getCenterY(), halfHeight, renderManager.getMaxY() - halfHeight);
        camera.update();
    }

    private void renderPauseMenu() {

        batch.draw(pauseMenuTexture, camera.position.x - (camera.viewportWidth / 4),
                camera.position.y - (camera.viewportHeight / 4), camera.viewportWidth / 2, camera.viewportHeight / 2);

    }

    private void renderSplashScreen() {

        batch.draw(splashTexture, camera.position.x - (camera.viewportWidth / 2),
                camera.position.y - (camera.viewportHeight / 2), camera.viewportWidth, camera.viewportHeight);

    }

    private void renderLoseScreen() {

        batch.draw(loseMenuTexture, camera.position.x - (camera.viewportWidth / 4), camera.position.y - (camera.viewportHeight / 4), camera.viewportWidth / 2, camera.viewportHeight / 2);

    }

    // Function to render the game screen, including the map, grid lines, and player.
    private void draw() {

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
        viewport.apply();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        if (renderManager.getInstance().STATE == State.PRE) {

            renderSplashScreen();
            batch.setProjectionMatrix(camera.combined);
            renderManager.getButtons().forEach(b -> b.draw(batch, renderManager.getInstance().STATE));
            batch.end();
            return;

        }

        if (renderManager.getInstance().STATE == State.PAUSE) {

            renderPauseMenu();
            batch.setProjectionMatrix(camera.combined);
            renderManager.getButtons().forEach(b -> b.draw(batch, renderManager.getInstance().STATE));
            batch.end();
            return;

        }

        if (renderManager.getInstance().STATE == State.LOSE) {

            renderLoseScreen();
            batch.setProjectionMatrix(camera.combined);
            renderManager.getButtons().forEach(b -> b.draw(batch, renderManager.getInstance().STATE));
            batch.end();
            return;

        }

        if (ticksRemaining == 0) {

            renderManager.getInstance().STATE = State.LOSE;
            this.renderManager.refreshButtons();
            batch.end();
            return;

        }

        // shapes.setProjectionMatrix(camera.combined); // turns world coords into screen positions
       // shapes.begin(ShapeType.Filled);
        // Walkable area (placeholder)
        // shapes.setColor(0.2f, 0.6f, 0.2f, 1f);
        batch.draw(mapTexture, 0, 0, renderManager.getMaxX(), renderManager.getMaxY());
        // shapes.rect(0, 0, renderManager.getMaxX(), renderManager.getMaxY());
        // shapes.end();

        // Grid lines (placeholder scenery) so movement is visible
        // shapes.begin(ShapeType.Line);
        // shapes.setColor(0.15f, 0.45f, 0.15f, 1f);
        // for (float x = 0; x <= renderManager.getMaxX(); x += GRID_SPACING) {
        // shapes.line(x, 0, x, renderManager.getMaxY());
        // }
        // for (float y = 0; y <= renderManager.getMaxY(); y += GRID_SPACING) {
        // shapes.line(0, y, renderManager.getMaxX(), y);
        // }
        // shapes.end();

        // Render all entities in entity manager
        // This will INCLUDE all dynamic and static entities ie trees
        int seconds = (int) Math.floor(ticksRemaining / 20f);
        int minutes = (int) Math.floor(seconds / 60f);
        int secondsAfterMinutes = seconds - (minutes * 60);

        if (secondsAfterMinutes < 10)
            font.draw(batch, minutes + ":0" + secondsAfterMinutes, camera.position.x + (camera.viewportWidth / 2) - 40, camera.position.y + (camera.viewportHeight / 2) - 15);
        else
            font.draw(batch, minutes + ":" + secondsAfterMinutes, camera.position.x + (camera.viewportWidth / 2) - 40, camera.position.y + (camera.viewportHeight / 2) - 15);

        batch.setProjectionMatrix(camera.combined);
        for (Entity e : renderManager.getInstance().getEntityManager().getEntities()) {

            batch.draw(e.getEntityTexture(), e.getPosX(), e.getPosY(),
                    (e.getEntityBoundingBox().getMaxX() - e.getEntityBoundingBox().getMinX()),
                    (e.getEntityBoundingBox().getMaxY() - e.getEntityBoundingBox().getMinY()));

        }
        renderManager.getButtons().forEach(b -> b.draw(batch, renderManager.getInstance().STATE));
        batch.end();
        System.out.println(renderManager.getInstance().getEntityManager().getEntities());
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
