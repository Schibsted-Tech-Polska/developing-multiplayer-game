package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexedDto<UnderlyingDto extends Dto> implements Dto {
    private final UnderlyingDto dto;
    private final long index;

    @JsonCreator
    public IndexedDto(
            @JsonProperty("dto") UnderlyingDto dto,
            @JsonProperty("index") long index) {
        this.dto = dto;
        this.index = index;
    }

    public UnderlyingDto getDto() {
        return dto;
    }

    public long getIndex() {
        return index;
    }
}
