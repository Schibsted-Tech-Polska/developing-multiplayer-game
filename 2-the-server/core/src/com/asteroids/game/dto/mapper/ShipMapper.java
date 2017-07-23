package com.asteroids.game.dto.mapper;

import com.asteroids.game.dto.ShipDto;
import com.asteroids.game.model.Ship;
import com.badlogic.gdx.math.Vector2;

public class ShipMapper {
    public static ShipDto fromShip(Ship ship) {
        Vector2 shipPosition = ship.getPosition();
        return new ShipDto(shipPosition.x, shipPosition.y, ship.getRotation());
    }
}
