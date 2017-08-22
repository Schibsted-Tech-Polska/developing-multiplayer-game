package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameStateDto implements Dto {
    private final List<PlayerDto> players;
    private final List<BulletDto> bullets;

    @JsonCreator
    public GameStateDto(
            @JsonProperty("players") List<PlayerDto> players,
            @JsonProperty("bullets") List<BulletDto> bullets) {
        this.players = players;
        this.bullets = bullets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameStateDto that = (GameStateDto) o;

        if (players != null ? !players.equals(that.players) : that.players != null) return false;
        return !(bullets != null ? !bullets.equals(that.bullets) : that.bullets != null);

    }

    @Override
    public int hashCode() {
        int result = players != null ? players.hashCode() : 0;
        result = 31 * result + (bullets != null ? bullets.hashCode() : 0);
        return result;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public List<BulletDto> getBullets() {
        return bullets;
    }
}
