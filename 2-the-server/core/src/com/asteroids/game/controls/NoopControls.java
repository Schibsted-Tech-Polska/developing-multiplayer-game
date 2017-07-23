package com.asteroids.game.controls;

public class NoopControls implements Controls {
    @Override
    public boolean forward() {
        return false;
    }

    @Override
    public boolean left() {
        return false;
    }

    @Override
    public boolean right() {
        return false;
    }

    @Override
    public boolean shoot() {
        return false;
    }
}
