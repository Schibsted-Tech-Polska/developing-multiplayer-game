package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IntroductoryStateDto implements Dto {
    private final PlayerDto connected;
    private final GameStateDto gameState;

    @JsonCreator
    public IntroductoryStateDto(
            @JsonProperty("connected") PlayerDto connected,
            @JsonProperty("gameState") GameStateDto gameState) {
        this.connected = connected;
        this.gameState = gameState;
    }

    public PlayerDto getConnected() {
        return connected;
    }

    public GameStateDto getGameState() {
        return gameState;
    }
}
