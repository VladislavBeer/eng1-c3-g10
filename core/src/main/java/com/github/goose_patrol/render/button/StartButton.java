package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

/** Button to start the game */
public class StartButton extends Button {

    public StartButton(float x, float y, float width, float height) {
        super("splash/BtnStart.png", State.PRE, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        renderManager.getInstance().STATE = State.PLAYING;

    }

}
