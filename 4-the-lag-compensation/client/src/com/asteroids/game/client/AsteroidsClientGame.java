package com.asteroids.game.client;

import com.asteroids.game.client.connection.Client;
import com.asteroids.game.client.connection.SocketIoClient;
import com.asteroids.game.client.connection.synchronization.LocalStateSynchronizer;
import com.asteroids.game.client.controls.KeyboardControls;
import com.asteroids.game.client.rendering.ContainerRenderer;
import com.asteroids.game.client.rendering.PlayerRenderer;
import com.asteroids.game.client.rendering.VisibleRenderer;
import com.asteroids.game.container.BulletsContainer;
import com.asteroids.game.container.Container;
import com.asteroids.game.container.PlayersContainer;
import com.asteroids.game.controls.Controls;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.asteroids.game.util.Delay;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static com.asteroids.game.AsteroidsGame.WORLD_HEIGHT;
import static com.asteroids.game.AsteroidsGame.WORLD_WIDTH;

public class AsteroidsClientGame extends Game {
    private Screen asteroids;

    @Override
    public void create() {
        Viewport viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT);
        ShapeRenderer shapeRenderer =  new ShapeRenderer();
        Controls localControls = new KeyboardControls();

        Container<Bullet> bulletsContainer = new BulletsContainer();
        PlayersContainer<Player> playersContainer = new PlayersContainer<>();

        ContainerRenderer<Bullet> bulletsRenderer = new ContainerRenderer<>(bulletsContainer, VisibleRenderer::new);
        ContainerRenderer<Player> playersRenderer = new ContainerRenderer<>(playersContainer, PlayerRenderer::new);

        Arena arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT);

        Map<String, String> env = System.getenv();
        String protocol = env.getOrDefault("PROTOCOL", "http");
        String host = env.getOrDefault("HOST", "localhost");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8080"));
        LocalStateSynchronizer localStateSynchronizer = new LocalStateSynchronizer();
        Client client = new SocketIoClient(protocol, host, port, new ReentrantLock(), localStateSynchronizer, new Delay(100));

        asteroids = new AsteroidsClientScreen(
                localControls, client, localStateSynchronizer,
                viewport, shapeRenderer,
                playersContainer, bulletsContainer,
                playersRenderer, bulletsRenderer, arena);

        setScreen(asteroids);
    }

    @Override
    public void dispose() {
        asteroids.dispose();
    }
}
