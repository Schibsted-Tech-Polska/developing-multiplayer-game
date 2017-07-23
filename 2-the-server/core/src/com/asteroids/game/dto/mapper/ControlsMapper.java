package com.asteroids.game.dto.mapper;

import com.asteroids.game.controls.RemoteControls;
import com.asteroids.game.dto.ControlsDto;

public class ControlsMapper {
    public static void setRemoteControlsByDto(ControlsDto dto, RemoteControls controls) {
        controls.setForward(dto.getForward());
        controls.setLeft(dto.getLeft());
        controls.setRight(dto.getRight());
        controls.setShoot(dto.getShoot());
    }
}
