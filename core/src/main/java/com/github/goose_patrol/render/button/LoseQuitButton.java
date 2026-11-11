package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

public class LoseQuitButton extends Button {

    public LoseQuitButton(float x, float y, float width, float height) {
        super("splash/quit.png", State.LOSE, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        System.exit(0);

    }

}
