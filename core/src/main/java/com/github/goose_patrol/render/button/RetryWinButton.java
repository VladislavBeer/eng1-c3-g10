package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

public class RetryWinButton extends Button {

    public RetryWinButton(float x, float y, float width, float height) {
        super("splash/retry.png", State.WIN, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        renderManager.getInstance().init();

    }

}
