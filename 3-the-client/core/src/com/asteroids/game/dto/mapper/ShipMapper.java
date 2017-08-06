package com.asteroids.game.dto.mapper;

import com.asteroids.game.dto.ShipDto;
import com.asteroids.game.model.Player;
import com.asteroids.game.model.Ship;
import com.badlogic.gdx.math.Vector2;

public class ShipMapper {
    public static ShipDto fromShip(Ship ship) {
        Vector2 shipPosition = ship.getPosition();
        return new ShipDto(shipPosition.x, shipPosition.y, ship.getRotation());
    }

    public static Ship fromDto(ShipDto dto, Player owner) {
        if(dto == null) return null;
        return new Ship(owner, new Vector2(dto.getX(), dto.getY()), dto.getRotation());
    }

    public static void updateByDto(Ship ship, ShipDto dto) {
        ship.setPosition(new Vector2(dto.getX(), dto.getY()));
        ship.setRotation(dto.getRotation());
    }
}
