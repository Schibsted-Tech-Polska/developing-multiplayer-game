package com.asteroids.game.controls;

public class RemoteControls implements Controls {
    private boolean forward;
    private boolean left;
    private boolean right;
    private boolean shoot;

    @Override
    public boolean forward() {
        return forward;
    }

    @Override
    public boolean left() {
        return left;
    }

    @Override
    public boolean right() {
        return right;
    }

    @Override
    public boolean shoot() {
        return shoot;
    }

    public void setForward(boolean state) {
        forward = state;
    }

    public void setLeft(boolean state) {
        left = state;
    }

    public void setRight(boolean state) {
        right = state;
    }

    public void setShoot(boolean state) {
        shoot = state;
    }
}
