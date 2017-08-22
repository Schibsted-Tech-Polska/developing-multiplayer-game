package com.asteroids.game.dto.mapper;

import com.asteroids.game.controls.Controls;
import com.asteroids.game.controls.RemoteControls;
import com.asteroids.game.dto.PlayerDto;
import com.asteroids.game.dto.ShipDto;
import com.asteroids.game.model.Player;
import com.asteroids.game.model.RemotePlayer;
import com.asteroids.game.model.Ship;
import com.badlogic.gdx.graphics.Color;

import java.util.Optional;
import java.util.UUID;

public class PlayerMapper {
    public static PlayerDto fromPlayer(Player player) {
        return new PlayerDto(player.getId().toString(), player.getColor().toString(),
                player.getShip()
                        .map(ShipMapper::fromShip)
                        .orElseGet(() -> null)
        );
    }

    public static RemotePlayer remotePlayerFromDto(PlayerDto dto) {
        return new RemotePlayer(UUID.fromString(dto.getId()), new RemoteControls(),
                    Color.valueOf(dto.getColor()));
    }

    public static Player localPlayerFromDto(PlayerDto dto, Controls controls) {
        Player player = new Player(UUID.fromString(dto.getId()), controls, Color.valueOf(dto.getColor()));
        player.setShip(ShipMapper.fromDto(dto.getShipDto(), player));
        return player;
    }

    public static void updateByDto(Player player, PlayerDto dto) {
        Optional<Ship> currentShip = player.getShip();
        ShipDto shipDto = dto.getShipDto();

        if(currentShip.isPresent() && shipDto != null) {
            ShipMapper.updateByDto(currentShip.get(), shipDto);
        }
        else {
            player.setShip(ShipMapper.fromDto(shipDto, player));
        }
    }
}
