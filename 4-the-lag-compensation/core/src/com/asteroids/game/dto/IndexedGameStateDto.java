package com.asteroids.game.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexedGameStateDto extends IndexedDto<GameStateDto> {
    @JsonCreator
    public IndexedGameStateDto(
            @JsonProperty("dto") GameStateDto dto,
            @JsonProperty("index") long index) {
        super(dto, index);
    }
}
