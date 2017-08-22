package com.asteroids.game.dto.mapper;

import com.asteroids.game.container.Container;
import com.asteroids.game.dto.BulletDto;
import com.asteroids.game.dto.GameStateDto;
import com.asteroids.game.dto.PlayerDto;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GameStateMapper {
    public static GameStateDto fromState(Container<? extends Player> players, Container<Bullet> bullets) {
        List<PlayerDto> playerDtos = players.stream()
                .map(PlayerMapper::fromPlayer)
                .collect(toList());
        List<BulletDto> bulletDtos = bullets.stream()
                .map(BulletMapper::fromBullet)
                .collect(toList());

        return new GameStateDto(playerDtos, bulletDtos);
    }
}
