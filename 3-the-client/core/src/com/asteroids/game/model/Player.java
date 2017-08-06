package com.asteroids.game.model;

import com.asteroids.game.controls.Controls;
import com.badlogic.gdx.graphics.Color;

import java.util.*;

public class Player implements Identifiable {
    public static final List<Color> POSSIBLE_COLORS = Collections.unmodifiableList(Arrays.asList(
            Color.WHITE, Color.GRAY, Color.BLUE, Color.GREEN, Color.ORANGE, Color.LIGHT_GRAY));
    private final UUID id;
    private final Controls controls;
    private final Color color;
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

    public void noticeHit() {
        this.ship = Optional.empty();
    }

    public void update(float delta) {
        ship.ifPresent(ship -> {
            ship.control(controls, delta);
            ship.update(delta);
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
