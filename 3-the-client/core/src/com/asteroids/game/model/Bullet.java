package com.asteroids.game.model;

import com.asteroids.game.util.Vectors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class Bullet implements Visible, Identifiable {
    private static final float[] VERTICES = new float[] {
            0, 0,
            2, 0,
            2, 2,
            0, 2
    };
    private static final float SPEED = 500f;
    private static final float RANGE = 400f;

    private final Player shooter;
    private final UUID id;
    private final Polygon shape;
    private float remainingRange;
    private boolean hasHitSomething;

    public Bullet(UUID id, Player shooter, Vector2 startPosition, float rotation) {
        shape = new Polygon(VERTICES);
        shape.setPosition(startPosition.x, startPosition.y);
        shape.setRotation(rotation);
        shape.setOrigin(0, -Ship.getMiddle().y);
        this.id = id;
        this.shooter = shooter;
        remainingRange = RANGE;
        hasHitSomething = false;
    }

    @Override
    public Color getColor() {
        return shooter.getColor();
    }

    @Override
    public Polygon getShape() {
        return shape;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getShooterId() {
        return shooter.getId();
    }

    public void move(float delta) {
        Vector2 direction = Vectors.getDirectionVector(shape.getRotation());
        Vector2 movement = new Vector2(direction.x * delta * SPEED, direction.y * delta * SPEED);
        remainingRange -= movement.len();
        shape.translate(movement.x, movement.y);
    }

    public boolean isInRange() {
        return remainingRange > 0;
    }

    public void noticeHit() {
        hasHitSomething = true;
    }

    public boolean hasHitSomething() {
        return hasHitSomething;
    }
}
