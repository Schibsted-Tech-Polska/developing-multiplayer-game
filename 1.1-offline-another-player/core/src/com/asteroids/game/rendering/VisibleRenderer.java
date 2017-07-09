package com.asteroids.game.rendering;

import com.asteroids.game.model.Visible;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class VisibleRenderer implements Renderer {
    private final Visible visible;

    public VisibleRenderer(Visible visible) {
        this.visible = visible;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(visible.getColor());
        shapeRenderer.polygon(visible.getShape().getTransformedVertices());
    }
}
