package com.asteroids.game.server;

import com.asteroids.game.container.BulletsContainer;
import com.asteroids.game.container.Container;
import com.asteroids.game.container.PlayersContainer;
import com.asteroids.game.dto.GameStateDto;
import com.asteroids.game.dto.PlayerDto;
import com.asteroids.game.dto.mapper.ControlsMapper;
import com.asteroids.game.dto.mapper.GameStateMapper;
import com.asteroids.game.dto.mapper.PlayerMapper;
import com.asteroids.game.manager.Collider;
import com.asteroids.game.manager.Respawner;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.Player;
import com.asteroids.game.model.RemotePlayer;
import com.asteroids.game.model.Ship;
import com.asteroids.game.server.connection.Server;
import com.badlogic.gdx.ScreenAdapter;

import java.util.Optional;
import java.util.stream.Stream;

public class AsteroidsServerScreen extends ScreenAdapter {
    private final Server server;
    private final PlayersContainer<RemotePlayer> playersContainer;
    private final BulletsContainer bulletsContainer;
    private final Arena arena;
    private final Respawner respawner;
    private final Collider collider;

    public AsteroidsServerScreen(Server server,
                                PlayersContainer<RemotePlayer> playersContainer, BulletsContainer bulletsContainer,
                                Arena arena, Respawner respawner, Collider collider) {
        this.server = server;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.arena = arena;
        this.respawner = respawner;
        this.collider = collider;
    }

    @Override
    public void show() {
        server.onPlayerConnected(playerDto -> {
            RemotePlayer connected = PlayerMapper.remotePlayerFromDto(playerDto);
            respawner.respawnFor(connected);
            PlayerDto connectedDto = PlayerMapper.fromPlayer(connected);
            GameStateDto gameStateDto = GameStateMapper.fromState(playersContainer, bulletsContainer);

            server.sendIntroductoryDataToConnected(connectedDto, gameStateDto);
            server.notifyOtherPlayersAboutConnected(connectedDto);
            playersContainer.add(connected);
        });

        server.onPlayerDisconnected(id -> {
            playersContainer.removeById(id);
            bulletsContainer.removeByPlayerId(id);
        });

        server.onPlayerSentControls((id, controlsDto) -> {
            playersContainer
                    .getById(id)
                    .ifPresent(sender -> ControlsMapper
                            .setRemoteControlsByDto(controlsDto, sender.getRemoteControls()));
        });

        server.start();
    }

    @Override
    public void render(float delta) {
        respawner.respawn();
        collider.checkCollisions();

        playersContainer.update(delta);
        playersContainer.streamShips()
                .forEach(arena::ensurePlacementWithinBounds);
        playersContainer.obtainAndStreamBullets()
                .forEach(bulletsContainer::add);

        bulletsContainer.update(delta);
        bulletsContainer.stream()
                .forEach(arena::ensurePlacementWithinBounds);

        server.broadcast(GameStateMapper.fromState(playersContainer, bulletsContainer));
    }
}
