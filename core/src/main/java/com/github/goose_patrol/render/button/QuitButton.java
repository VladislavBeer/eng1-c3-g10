package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

public class QuitButton extends Button {

    public QuitButton(float x, float y, float width, float height) {
        super("splash/quit.png", State.PAUSE, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        System.exit(0);

    }

}
