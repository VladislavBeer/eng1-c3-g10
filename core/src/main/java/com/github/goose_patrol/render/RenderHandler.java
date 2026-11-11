package com.github.goose_patrol.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.github.goose_patrol.util.State;

// Main gameplay screen: reads input, updates the player and draws the map and player.
public class RenderHandler extends ScreenAdapter {
    private static final float VIEW_WIDTH = 640f;
    private static final float VIEW_HEIGHT = 480f;
    // Spacing of the placeholder grid lines that make scrolling visible. Take out once we have a proper map.
    private static final float GRID_SPACING = 100f;


    private Stage stage;

    // Timer logic
    private float timeElapsed = 0;
    private final float MAX_TIME = 300;
    private final float buttonX;
    private final float buttonY;
    private final float buttonWidth;
    private final float buttonHeight;
    private boolean gameOver = false;

    private long lastTick;

    private final Texture splashTexture;
    private final Texture startBtnTexture;
    private final Texture mapTexture;
    private final Player player;
    private final RenderManager renderManager;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final FitViewport viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();

    public RenderHandler(Player player, RenderManager manager) {
        this.player = player;
        batch.setProjectionMatrix(camera.combined);
        viewport.apply();
        this.mapTexture = new Texture("static/map_base.png");
        this.splashTexture = new Texture("splash/splash.png");
        this.startBtnTexture = new Texture("splash/BtnStart.png");
        this.buttonX = camera.position.x  - (camera.viewportWidth / 8);
        this.buttonY = camera.position.y + 25 - (camera.viewportHeight / 2);
        this.buttonWidth = (camera.position.x  + (camera.viewportWidth / 8)) - (camera.position.x  - (camera.viewportWidth / 8));
        this.buttonHeight = camera.position.y + (camera.viewportHeight / 8);
        this.renderManager = manager;
    }

    private void checkButtonClicked () {

        float mouseX = getMousePosGaame().x;
        float mouseY = getMousePosGaame().y;
        if (mouseX > (buttonX + 20) && mouseX < (buttonX + buttonWidth - 20) && mouseY > (buttonY + 10) && mouseY < (buttonY + buttonHeight - 5)) {

            Gdx.graphics.setSystemCursor(SystemCursor.Hand);
            if (Gdx.input.isButtonPressed(0)) {
                renderManager.getInstance().STATE = State.PLAYING;
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }
            return;

        }

        Gdx.graphics.setSystemCursor(SystemCursor.Arrow);

    }

    Vector3 getMousePosGaame() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void render(float delta) {


        /*
            We want to tick the game 20 times per second
            We can use the render method to do this
        */
       if (renderManager.getInstance().STATE == State.PRE) {
            draw();
            checkButtonClicked();
            return;
       }
        long tickOffset = System.currentTimeMillis() - lastTick;
        if (tickOffset >= 20L) {
            // TICK
            update(delta);
            lastTick = System.currentTimeMillis();
        }

        // Only update the timer if game is still active
        if (!gameOver) {

            if (timeElapsed >= MAX_TIME){
                gameOver = true;
            }
            
            // ADD GAME OVER LOGIC HERE
        }


        draw();
    }

    // On press of WASD / arrow keys, moves the player
    private void update(float delta) {

        this.renderManager.getInstance().getEntityManager().tickEntities(delta);

        // Keep the player centred but if they try walk past the edge, reset their position before the render is updated.
        float halfWidth = viewport.getWorldWidth() / 2;
        float halfHeight = viewport.getWorldHeight() / 2;
        camera.position.x = MathUtils.clamp(player.getCenterX(), halfWidth, renderManager.getMaxX() - halfWidth);
        camera.position.y = MathUtils.clamp(player.getCenterY(), halfHeight, renderManager.getMaxY() - halfHeight);
        camera.update();
    }

    private void renderSplashScreen() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(splashTexture, camera.position.x - (camera.viewportWidth / 2), camera.position.y - (camera.viewportHeight / 2), camera.viewportWidth, camera.viewportHeight);
        batch.draw(startBtnTexture, buttonX, buttonY, buttonWidth, buttonHeight);
        batch.end();


    }

    // Function to render the game screen, including the map, grid lines, and player.
    private void draw() {

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
        viewport.apply();

        if (renderManager.getInstance().STATE == State.PRE) {

            renderSplashScreen();
            return;

        }

       // shapes.setProjectionMatrix(camera.combined); // turns world coords into screen positions
       // shapes.begin(ShapeType.Filled);
        // Walkable area (placeholder)
       // shapes.setColor(0.2f, 0.6f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(mapTexture, 0, 0, renderManager.getMaxX(), renderManager.getMaxY());
        batch.end();
       // shapes.rect(0, 0, renderManager.getMaxX(), renderManager.getMaxY());
       // shapes.end();

        // Grid lines (placeholder scenery) so movement is visible
       // shapes.begin(ShapeType.Line);
        //shapes.setColor(0.15f, 0.45f, 0.15f, 1f);
        //for (float x = 0; x <= renderManager.getMaxX(); x += GRID_SPACING) {
        //    shapes.line(x, 0, x, renderManager.getMaxY());
        //}
        //for (float y = 0; y <= renderManager.getMaxY(); y += GRID_SPACING) {
        //    shapes.line(0, y, renderManager.getMaxX(), y);
        //}
        //shapes.end();

        // Render all entities in entity manager
        // This will INCLUDE all dynamic and static entities ie trees
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity e : renderManager.getInstance().getEntityManager().getEntities()) {

            batch.draw(e.getEntityTexture(), e.getPosX(), e.getPosY(), (e.getEntityBoundingBox().getMaxX() - e.getEntityBoundingBox().getMinX()), (e.getEntityBoundingBox().getMaxY() - e.getEntityBoundingBox().getMinY()));

        }
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