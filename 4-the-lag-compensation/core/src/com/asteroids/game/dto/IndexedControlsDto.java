package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexedControlsDto extends IndexedDto<ControlsDto> {
    @JsonCreator
    public IndexedControlsDto(
            @JsonProperty("dto") ControlsDto dto,
            @JsonProperty("index") long index) {
        super(dto, index);
    }
}
