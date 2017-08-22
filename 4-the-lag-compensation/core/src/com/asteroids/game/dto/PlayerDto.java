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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerDto playerDto = (PlayerDto) o;

        if (id != null ? !id.equals(playerDto.id) : playerDto.id != null) return false;
        if (color != null ? !color.equals(playerDto.color) : playerDto.color != null) return false;
        return !(shipDto != null ? !shipDto.equals(playerDto.shipDto) : playerDto.shipDto != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (shipDto != null ? shipDto.hashCode() : 0);
        return result;
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
