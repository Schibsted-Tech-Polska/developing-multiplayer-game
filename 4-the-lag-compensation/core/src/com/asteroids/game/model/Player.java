package com.asteroids.game.model;

import com.asteroids.game.controls.Controls;
import com.badlogic.gdx.graphics.Color;

import java.util.*;

public class Player implements Identifiable {
    public static final List<Color> POSSIBLE_COLORS = Collections.unmodifiableList(Arrays.asList(
            Color.WHITE, Color.GRAY, Color.BLUE, Color.GREEN, Color.ORANGE, Color.LIGHT_GRAY));
    private final UUID id;
    private final Color color;
    private Controls controls;
    private Optional<Ship> ship;

    public Player(UUID id, Controls controls, Color color) {
        this.id = id;
        this.controls = controls;
        this.color = color;
        this.ship = Optional.empty();
    }

    public void setShip(Ship ship) {
        this.ship = Optional.ofNullable(ship);
    }

    public void setControls(Controls controls) {
        this.controls = controls;
    }

    public Controls getControls() {
        return controls;
    }

    public void noticeHit() {
        this.ship = Optional.empty();
    }

    public void update() {
        ship.ifPresent(Ship::update);
    }

    public void move(float delta) {
        ship.ifPresent(ship -> {
            ship.control(controls, delta);
            ship.move(delta);
        });
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Optional<Ship> getShip() {
        return ship;
    }

    public Color getColor() {
        return color;
    }
}
