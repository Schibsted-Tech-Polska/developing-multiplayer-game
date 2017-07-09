package com.asteroids.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;

public interface Visible {
    Color getColor();
    Polygon getShape();
}
