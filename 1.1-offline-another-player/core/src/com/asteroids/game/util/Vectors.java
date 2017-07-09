package com.asteroids.game.util;

import com.badlogic.gdx.math.Vector2;

public class Vectors {
    public static Vector2 getDirectionVector(float rotation) {
        return new Vector2(-(float)Math.sin(Math.toRadians(rotation)), (float)Math.cos(Math.toRadians(rotation)));
    }
}
