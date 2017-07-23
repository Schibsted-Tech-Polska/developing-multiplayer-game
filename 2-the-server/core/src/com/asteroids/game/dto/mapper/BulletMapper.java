package com.asteroids.game.dto.mapper;

import com.asteroids.game.dto.BulletDto;
import com.asteroids.game.model.Bullet;
import com.badlogic.gdx.math.Vector2;

public class BulletMapper {
    public static BulletDto fromBullet(Bullet bullet) {
        Vector2 position = bullet.getPosition();
        return new BulletDto(bullet.getId().toString(),
                position.x, position.y, bullet.getRotation(),
                bullet.getShooterId().toString());
    }
}
