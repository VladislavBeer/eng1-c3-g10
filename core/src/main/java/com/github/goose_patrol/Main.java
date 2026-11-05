package com.github.goose_patrol;

import com.badlogic.gdx.Game;

/** Entry point shared by all platforms. Starts the game on the {@link GameScreen}. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        // Game.dispose() only hides the current screen, so dispose of it explicitly.
        if (getScreen() != null) {
            getScreen().dispose();
        }
        super.dispose();
    }
}