package com.asteroids.game.dto.mapper;

import com.asteroids.game.controls.RemoteControls;
import com.asteroids.game.dto.PlayerDto;
import com.asteroids.game.model.Player;
import com.asteroids.game.model.RemotePlayer;
import com.badlogic.gdx.graphics.Color;

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
}
