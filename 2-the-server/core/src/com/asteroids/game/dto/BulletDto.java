package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BulletDto implements Dto {
    private final String id;
    private final float x;
    private final float y;
    private final float rotation;
    private final String shooterId;

    @JsonCreator
    public BulletDto(
            @JsonProperty("id") String id,
            @JsonProperty("x") float x,
            @JsonProperty("y") float y,
            @JsonProperty("rotation") float rotation,
            @JsonProperty("shooterId") String shooterId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.shooterId = shooterId;
    }

    public String getId() {
        return id;
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

    public String getShooterId() {
        return shooterId;
    }
}
