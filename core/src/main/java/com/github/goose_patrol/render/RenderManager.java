package com.github.goose_patrol.render;

import com.github.goose_patrol.GoosePatrol;
import com.github.goose_patrol.entity.player.Player;
import com.github.goose_patrol.render.button.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for the render handler
 * <p>
 * For use with all classes to access attributes and methods of the render
 * handler
 */
public class RenderManager {

    private String backgroundFile;
    private final float maxX;
    private final float maxY;
    private RenderHandler renderHandler;
    private GoosePatrol instance;
    private List<Button> buttons;

    public RenderManager(GoosePatrol instance, float width, float height, Player player) {
        this.maxX = width;
        this.instance = instance;
        this.maxY = height;
        this.buttons = new ArrayList<>();
        this.renderHandler = new RenderHandler(player, this);
        refreshButtons();
    }

    public void refreshButtons() {

        buttons.clear();

        float buttonWidth = 160F;
        float buttonHeight = 60F;

        // SPLASH
        buttons.add(new StartButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y + 25 - (renderHandler.camera.viewportHeight / 2),
                buttonWidth,
                buttonHeight));

        // PAUSE
        buttons.add(new ResumeButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y - (renderHandler.camera.viewportHeight / 12),
                buttonWidth,
                buttonHeight));
        buttons.add(new QuitButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y - (renderHandler.camera.viewportHeight / 6),
                buttonWidth,
                buttonHeight));

        // WIN
        buttons.add(new RetryWinButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y - (renderHandler.camera.viewportHeight / 12) - 20,
                buttonWidth,
                buttonHeight));
        buttons.add(new WinQuitButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y - (renderHandler.camera.viewportHeight / 6) - 20,
                buttonWidth,
                buttonHeight));

        // LOSE
        buttons.add(new RetryButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y - (renderHandler.camera.viewportHeight / 12) - 20,
                buttonWidth,
                buttonHeight));
        buttons.add(new LoseQuitButton(renderHandler.camera.position.x - (renderHandler.camera.viewportWidth / 8),
                renderHandler.camera.position.y - (renderHandler.camera.viewportHeight / 6) - 20,
                buttonWidth,
                buttonHeight));

    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void addButton(Button button) {
        buttons.add(button);
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

    public GoosePatrol getInstance() {
        return this.instance;
    }

}
