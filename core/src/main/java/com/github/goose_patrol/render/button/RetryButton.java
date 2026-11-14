package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

/** Button to retry the game on losing */
public class RetryButton extends Button {

    public RetryButton(float x, float y, float width, float height) {
        super("splash/retry.png", State.LOSE, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        renderManager.getInstance().init();

    }

}
