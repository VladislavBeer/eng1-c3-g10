package com.github.goose_patrol.render.button;

import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

/** button to continue the game from the pause screen */
public class ResumeButton extends Button {

    public ResumeButton(float x, float y, float width, float height) {
        super("splash/resume.png", State.PAUSE, x, y, width, height);
    }

    @Override
    public void onClick(RenderManager renderManager) {

        renderManager.getInstance().STATE = State.PLAYING;

    }

}
