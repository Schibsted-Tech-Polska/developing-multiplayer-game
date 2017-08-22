package com.asteroids.game.dto.mapper;

import com.asteroids.game.container.Container;
import com.asteroids.game.dto.BulletDto;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class BulletMapper {
    public static BulletDto fromBullet(Bullet bullet) {
        Vector2 position = bullet.getPosition();
        return new BulletDto(bullet.getId().toString(),
                position.x, position.y, bullet.getRotation(),
                bullet.getShooterId().toString());
    }

    public static Bullet fromDto(BulletDto dto, Container<Player> playersContainer) {
        Player shooter = playersContainer.getById(dto.getShooterId())
                .orElseThrow(() -> new RuntimeException("Cannot find Player of id " + dto.getShooterId() + " to create a Bullet."));
        return new Bullet(UUID.fromString(dto.getId()), shooter,
                new Vector2(dto.getX(), dto.getY()), dto.getRotation());
    }

    public static void updateByDto(Bullet bullet, BulletDto dto) {
        bullet.setPosition(new Vector2(dto.getX(), dto.getY()));
    }
}
