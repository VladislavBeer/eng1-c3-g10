package com.github.goose_patrol.render;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.github.goose_patrol.entity.Entity;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.render.button.Button;
import com.github.goose_patrol.event.HiddenEvent1;
import com.github.goose_patrol.event.HiddenEvent2;
import com.github.goose_patrol.util.State;

/**
 * Main gameplay screen: reads input, updates the player and draws the map and
 * player
 * <p>
 * Defines all ui elements and entities and textures to be placed on the screen
 * <p>
 * Main game loop will tick all entities in entity manager and updates positions
 * and states, before rendering and drawing them all to the screen
 * <p>
 */
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
    private final Texture winTexture;
    private final Texture pauseMenuTexture;
    private final Texture loseMenuTexture;
    private final Texture mapTexture;
    private final Texture Inventory_Slot;
    private final Texture Inventory_Slot_Selected;
    private final Player player;
    private final RenderManager renderManager;
    private final HiddenEvent1 hiddenEvent1;
    private final HiddenEvent2 hiddenEvent2;
    // private final Texture busStopTexture;
    // private final Texture tree1Texture;
    // private final Texture tree2Texture;
    // private final Texture tree3Texture;
    // private final Texture tree4Texture;
    // private final Texture tree5Texture;
    // private final Texture tree6Texture;
    final OrthographicCamera camera = new OrthographicCamera();
    private final FitViewport viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final ArrayList<Entity> tickTimeEntities;

    private int Event_Triggered_Count = 0;

    public RenderHandler(Player player, RenderManager manager) {
        this.player = player;
        batch.setProjectionMatrix(camera.combined);
        viewport.apply();
        this.font = new BitmapFont();
        ticksRemaining = 6000;
        this.mapTexture = new Texture("static/map_base.png");
        this.splashTexture = new Texture("splash/splash.png");
        this.pauseMenuTexture = new Texture("splash/pause.png");
        this.loseMenuTexture = new Texture("splash/lose.png");
        this.winTexture = new Texture("splash/win.png");
        this.Inventory_Slot = new Texture("Inventory/slot_normal.png");
        this.Inventory_Slot_Selected = new Texture("Inventory/slot_selected.png");
        this.renderManager = manager;
        this.hiddenEvent1 = new HiddenEvent1(manager.getInstance());
        this.hiddenEvent2 = new HiddenEvent2(manager.getInstance());
        this.tickTimeEntities = new ArrayList<>();
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

    /** On press of WASD / arrow keys, moves the player */
    private void update(float delta) {

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {

            this.renderManager.refreshButtons();
            renderManager.getInstance().STATE = State.PAUSE;
            return;
        }

        this.renderManager.getInstance().getEntityManager().tickEntities(delta);

        // Adding entities created during tick time
        for (Entity e : tickTimeEntities) {
            this.renderManager.getInstance().getEntityManager().addEntity(e);
        }
        tickTimeEntities.clear();

        if ((this.player.getPosX() >= 924f) && (this.player.getPosX() <= 1264f) && (this.player.getPosY() >= 0f)
                && (this.player.getPosY() <= 425f) && (!this.hiddenEvent1.hasTriggered())) {
            this.hiddenEvent1.activate();
            Event_Triggered_Count++;
        } else if ((this.player.getPosX() >= 1225f) && (this.player.getPosX() <= 1550f)
                && (this.player.getPosY() >= 769f) && (this.player.getPosY() <= 1044f)
                && (!this.hiddenEvent2.hasTriggered())) {
            this.hiddenEvent2.activate();
            Event_Triggered_Count++;
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

    private void renderWinMenu() {

        batch.draw(winTexture, camera.position.x - (camera.viewportWidth / 4),
                camera.position.y - (camera.viewportHeight / 4), camera.viewportWidth / 2, camera.viewportHeight / 2);

    }

    private void renderLoseScreen() {

        batch.draw(loseMenuTexture, camera.position.x - (camera.viewportWidth / 4),
                camera.position.y - (camera.viewportHeight / 4), camera.viewportWidth / 2, camera.viewportHeight / 2);

    }

    /**
     * Function to render the game screen, including the map, grid lines, and
     * player.
     */
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

        if (renderManager.getInstance().STATE == State.WIN) {

            renderWinMenu();
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

        // shapes.setProjectionMatrix(camera.combined); // turns world coords into
        // screen positions
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
        // buildings
        // batch.draw(buildingTexture, 75, 225, 300, 150);
        // batch.draw(busStopTexture, 75, 1040, 200, 100);
        // trees
        // batch.draw(tree1Texture,10, 230, 80, 150);
        // batch.draw(tree1Texture, 375, 230, 70, 160);

        // batch.draw(tree1Texture, 1375, 100, 70, 160);
        // batch.draw(tree1Texture, 1450, 170, 70, 160);
        // batch.draw(tree1Texture, 1490, 50, 70, 160);
        // oak trees
        // batch.draw(tree2Texture, 1300, 1100, 90, 100);
        // Trees around the lake edges
        // batch.draw(tree3Texture, 575, 330, 78, 90);
        // batch.draw(tree3Texture, 760, 370, 81, 100);
        // batch.draw(tree3Texture, 1000, 450, 82, 70);
        // batch.draw(tree3Texture, 1220, 650, 76, 80);
        // birch trees
        // batch.draw(tree4Texture, 1000, 800, 90, 150);
        // batch.draw(tree4Texture, 1090, 750, 100, 160);
        // batch.draw(tree4Texture, 1170, 820, 110, 110);
        // mangrove trees
        // batch.draw(tree5Texture, 1200, 290, 80, 75);
        // batch.draw(tree5Texture, 1000, 360, 80, 100);
        // batch.draw(tree5Texture, 1156, 420, 80, 75);

        // cherry blossom trees
        // batch.draw(tree6Texture, 190, 800, 120, 140);
        // batch.draw(tree6Texture, 325, 820, 110, 130);
        // batch.draw(tree6Texture, 430, 1080, 90, 110);
        // batch.draw(tree6Texture, 550, 1090, 90, 105);
        // batch.draw(tree6Texture, 700, 1070, 85, 100);
        // batch.draw(tree6Texture, 890, 1100, 80, 90);

        // render entities
        batch.setProjectionMatrix(camera.combined);
        for (Entity e : renderManager.getInstance().getEntityManager().getEntities()) {

            // use the entity's texture region to draw it at its position with its bounding box size.
            // If you use the entity's texture directly, scaling might not work as expected, and animated sprites might not display correctly. 
            // Using the texture region ensures that the correct portion of the texture is drawn, especially if the entity has animations or different states.
            TextureRegion region = e.getEntityTexture();
            if (region == null)
                continue; // Skip rendering if the texture is null

            batch.draw(region, e.getPosX(), e.getPosY(),
                    (e.getEntityBoundingBox().getMaxX() - e.getEntityBoundingBox().getMinX()),
                    (e.getEntityBoundingBox().getMaxY() - e.getEntityBoundingBox().getMinY()));

        }
        renderManager.getButtons().forEach(b -> b.draw(batch, renderManager.getInstance().STATE));

        // Timer
        int seconds = (int) Math.floor(ticksRemaining / 20f);
        int minutes = (int) Math.floor(seconds / 60f);
        int secondsAfterMinutes = seconds - (minutes * 60);

        if (secondsAfterMinutes < 10)
            font.draw(batch, minutes + ":0" + secondsAfterMinutes, camera.position.x + (camera.viewportWidth / 2) - 40,
                    camera.position.y + (camera.viewportHeight / 2) - 15);
        else
            font.draw(batch, minutes + ":" + secondsAfterMinutes, camera.position.x + (camera.viewportWidth / 2) - 40,
                    camera.position.y + (camera.viewportHeight / 2) - 15);

        // Events counter
        font.draw(batch, "Events Triggered: " + Event_Triggered_Count,
                camera.position.x - (camera.viewportWidth / 2) + 10,
                camera.position.y + (camera.viewportHeight / 2) - 15);

        // Items used counter
        font.draw(batch, "Items Used: " + player.Get_Number_Of_Items_Used(),
                camera.position.x - (camera.viewportWidth / 2) + 10,
                camera.position.y + (camera.viewportHeight / 2) - 35);

        // Inventory
        if (player.getInventory() != null) {
            for (int i = 0; i < player.getInventory().length; i++) { // Drawing the items in the inventory
                if (player.getHeldItem() == i) {
                    batch.draw(Inventory_Slot_Selected, camera.position.x - (100) + 10 + (i * 30),
                            camera.position.y - (camera.viewportHeight / 2) + 20, 30, 30);
                } else {
                    batch.draw(Inventory_Slot, camera.position.x - (100) + 10 + (i * 30),
                            camera.position.y - (camera.viewportHeight / 2) + 20, 30, 30);
                }
                if (player.getInventory()[i] != null) {
                    batch.draw(player.getInventory()[i].getImageItem(), camera.position.x - (100) + 10 + (i * 30),
                            camera.position.y - (camera.viewportHeight / 2) + 20, 30, 30);
                    // if (player.getInventory()[i] != null) {
                    // batch.draw(new Texture(player.getInventory()[i].getImageItem()),
                    // camera.position.x - (100) + 10 + (i * 30),
                    // camera.position.y - (camera.viewportHeight / 2) + 20, 30, 30);
                }
            }
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
        player.dispose();
    }

    public void addTickTimeEntity(Entity entity) {
        tickTimeEntities.add(entity);
    }
}
