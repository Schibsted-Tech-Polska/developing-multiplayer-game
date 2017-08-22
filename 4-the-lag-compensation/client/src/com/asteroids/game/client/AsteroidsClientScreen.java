package com.asteroids.game.client;

import com.asteroids.game.client.connection.Client;
import com.asteroids.game.client.connection.synchronization.LocalStateSynchronizer;
import com.asteroids.game.client.rendering.ContainerRenderer;
import com.asteroids.game.container.Container;
import com.asteroids.game.container.PlayersContainer;
import com.asteroids.game.controls.Controls;
import com.asteroids.game.controls.NoopControls;
import com.asteroids.game.dto.BulletDto;
import com.asteroids.game.dto.GameStateDto;
import com.asteroids.game.dto.PlayerDto;
import com.asteroids.game.dto.mapper.BulletMapper;
import com.asteroids.game.dto.mapper.ControlsMapper;
import com.asteroids.game.dto.mapper.GameStateMapper;
import com.asteroids.game.dto.mapper.PlayerMapper;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.asteroids.game.util.Randomize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AsteroidsClientScreen extends ScreenAdapter {
    private final Controls localControls;
    private final Client client;
    private final LocalStateSynchronizer localStateSynchronizer;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final PlayersContainer<Player> playersContainer;
    private final Container<Bullet> bulletsContainer;
    private final ContainerRenderer<Player> playersRenderer;
    private final ContainerRenderer<Bullet> bulletsRenderer;
    private final Arena arena;
    private Player localPlayer;

    public AsteroidsClientScreen(
            Controls localControls, Client client,  LocalStateSynchronizer localStateSynchronizer,
            Viewport viewport, ShapeRenderer shapeRenderer,
            PlayersContainer<Player> playersContainer, Container<Bullet> bulletsContainer,
            ContainerRenderer<Player> playersRenderer, ContainerRenderer<Bullet> bulletsRenderer,
            Arena arena) {
        this.localControls = localControls;
        this.client = client;
        this.localStateSynchronizer = localStateSynchronizer;
        this.viewport = viewport;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.shapeRenderer = shapeRenderer;
        this.playersRenderer = playersRenderer;
        this.bulletsRenderer = bulletsRenderer;
        this.arena = arena;
    }

    @Override
    public void show() {
        client.onConnected(introductoryStateDto -> {
            localPlayer = PlayerMapper.localPlayerFromDto(introductoryStateDto.getConnected(), localControls);
            localStateSynchronizer.setLocalPlayer(localPlayer);
            playersContainer.add(localPlayer);

            GameStateDto gameStateDto = introductoryStateDto.getGameState();
            gameStateDto.getPlayers().stream()
                    .map(playerDto -> PlayerMapper.localPlayerFromDto(playerDto, new NoopControls()))
                    .forEach(playersContainer::add);

            gameStateDto.getBullets().stream()
                    .map(bulletDto -> BulletMapper.fromDto(bulletDto, playersContainer))
                    .forEach(bulletsContainer::add);
        });

        client.onOtherPlayerConnected(connectedDto -> {
            Player connected = PlayerMapper.localPlayerFromDto(connectedDto, new NoopControls());
            playersContainer.add(connected);
        });

        client.onOtherPlayerDisconnected(uuidDto -> playersContainer.removeById(uuidDto.getUuid()));

        client.onGameStateReceived(gameStateDto -> {
            gameStateDto.getBullets().stream()
                    .forEach(bulletDto -> {
                        Optional<Bullet> bullet = bulletsContainer.getById(bulletDto.getId());
                        if(!bullet.isPresent()) {
                            bulletsContainer.add(BulletMapper.fromDto(bulletDto, playersContainer));
                        } else {
                            BulletMapper.updateByDto(bullet.get(), bulletDto);
                        }
                    });

            List<String> existingBulletIds = gameStateDto.getBullets().stream()
                    .map(BulletDto::getId)
                    .collect(toList());

            bulletsContainer.getAll().stream()
                    .map(Bullet::getId)
                    .map(Object::toString)
                    .filter(id -> !existingBulletIds.contains(id))
                    .collect(toList())
                    .forEach(bulletsContainer::removeById);
        });

        localStateSynchronizer.updateAccordingToGameState(gameStateDto -> {
            gameStateDto.getPlayers().stream()
                    .forEach(playerDto -> playersContainer
                            .getById(playerDto.getId())
                            .ifPresent(player -> PlayerMapper.updateByDto(player, playerDto)));
        });

        localStateSynchronizer.supplyGameState(() -> GameStateMapper.fromState(playersContainer, bulletsContainer));

        localStateSynchronizer.runGameLogic(this::runGameLogic);

        client.connect(new PlayerDto(null, Randomize.fromList(Player.POSSIBLE_COLORS).toString(), null));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!client.isConnected()) return;
        client.lockEventListeners();

        client.sendControls(ControlsMapper.mapToDto(localControls));
        runGameLogic(delta);
        localStateSynchronizer.recordState(delta,
                ControlsMapper.mapToDto(localControls));

        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        playersRenderer.render(shapeRenderer);
        bulletsRenderer.render(shapeRenderer);
        shapeRenderer.end();
        client.unlockEventListeners();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void runGameLogic(float delta) {
        playersContainer.streamShips()
                .forEach(arena::ensurePlacementWithinBounds);
        playersContainer.move(delta);
    }
}
