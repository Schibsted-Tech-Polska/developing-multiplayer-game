package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class GameStateDto implements Dto {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final List<PlayerDto> players;
    private final List<BulletDto> bullets;

    @JsonCreator
    public GameStateDto(
            @JsonProperty("players") List<PlayerDto> players,
            @JsonProperty("bullets") List<BulletDto> bullets) {
        this.players = players;
        this.bullets = bullets;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public List<BulletDto> getBullets() {
        return bullets;
    }
}
