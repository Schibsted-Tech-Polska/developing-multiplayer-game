package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipDto implements Dto {
    private final float x;
    private final float y;
    private final float rotation;
    private final float velocityX;
    private final float velocityY;
    private final float rotationVelocity;

    @JsonCreator
    public ShipDto(
            @JsonProperty("x") float x,
            @JsonProperty("y") float y,
            @JsonProperty("rotation") float rotation,
            @JsonProperty("velocityX") float velocityX,
            @JsonProperty("velocityY") float velocityY,
            @JsonProperty("rotationVelocity") float rotationVelocity) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.rotationVelocity = rotationVelocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipDto shipDto = (ShipDto) o;

        if (Float.compare(shipDto.x, x) != 0) return false;
        if (Float.compare(shipDto.y, y) != 0) return false;
        if (Float.compare(shipDto.rotation, rotation) != 0) return false;
        if (Float.compare(shipDto.velocityX, velocityX) != 0) return false;
        if (Float.compare(shipDto.velocityY, velocityY) != 0) return false;
        return Float.compare(shipDto.rotationVelocity, rotationVelocity) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (rotation != +0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + (velocityX != +0.0f ? Float.floatToIntBits(velocityX) : 0);
        result = 31 * result + (velocityY != +0.0f ? Float.floatToIntBits(velocityY) : 0);
        result = 31 * result + (rotationVelocity != +0.0f ? Float.floatToIntBits(rotationVelocity) : 0);
        return result;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getRotationVelocity() {
        return rotationVelocity;
    }
}
