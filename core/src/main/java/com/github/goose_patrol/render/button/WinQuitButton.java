package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

/** Button to quit the game after winning */
public class WinQuitButton extends Button {

    public WinQuitButton(float x, float y, float width, float height) {
        super("splash/quit.png", State.WIN, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        System.exit(0);

    }

}
