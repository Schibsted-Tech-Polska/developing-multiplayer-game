package com.asteroids.game.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerDto implements Dto {
    private final String id;
    private final String color;
    private final ShipDto shipDto;

    @JsonCreator
    public PlayerDto(
            @JsonProperty("id") String id,
            @JsonProperty("color") String color,
            @JsonProperty("ship") ShipDto shipDto) {
        this.id = id;
        this.color = color;
        this.shipDto = shipDto;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public ShipDto getShipDto() {
        return shipDto;
    }
}
