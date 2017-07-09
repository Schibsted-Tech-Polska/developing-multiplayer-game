package com.asteroids.game.controls;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.Input.Keys.*;

public class KeyboardControls implements Controls {
    @Override
    public boolean forward() {
        return Gdx.input.isKeyPressed(UP);
    }

    @Override
    public boolean left() {
        return Gdx.input.isKeyPressed(LEFT);
    }

    @Override
    public boolean right() {
        return Gdx.input.isKeyPressed(RIGHT);
    }

    @Override
    public boolean shoot() {
        return Gdx.input.isKeyPressed(SPACE);
    }
}
