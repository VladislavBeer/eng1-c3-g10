package com.github.goose_patrol.render.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.goose_patrol.render.RenderManager;
import com.github.goose_patrol.util.State;

public class Button {

    private final String path;
    private final Enum stateScope;
    private final Texture texture;
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public Button(String path, State stateScope, float x, float y, float width, float height) {
        this.path = path;
        this.stateScope = stateScope;
        this.texture = new Texture(path);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Batch batch, State state) {

        if (this.stateScope != state) return;

        batch.draw(texture, x, y, width, height);

    }

    public boolean handleClick(float mouseX, float mouseY, RenderManager renderManager) {

        if (this.stateScope != renderManager.getInstance().STATE) return false;
        if (mouseX > (x + 20) && mouseX < (x + width - 20) && mouseY > (y + 10) && mouseY < (y + height - 5)) {

            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            if (Gdx.input.isButtonPressed(0)) {
                onClick(renderManager);
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
            return true;

        }

        return false;

    }

    // TO BE OVERRIDDEN IN CHILDREN CLASSES
    public void onClick(RenderManager manager) {




    }


}
