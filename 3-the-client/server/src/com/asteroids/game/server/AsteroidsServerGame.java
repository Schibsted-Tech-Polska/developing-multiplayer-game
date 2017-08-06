package com.asteroids.game.server;

import com.asteroids.game.container.BulletsContainer;
import com.asteroids.game.container.PlayersContainer;
import com.asteroids.game.manager.Collider;
import com.asteroids.game.manager.Respawner;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.RemotePlayer;
import com.asteroids.game.server.connection.Server;
import com.asteroids.game.server.connection.SocketIoServer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.Map;

import static com.asteroids.game.AsteroidsGame.WORLD_HEIGHT;
import static com.asteroids.game.AsteroidsGame.WORLD_WIDTH;

public class AsteroidsServerGame extends Game {
    private Screen asteroids;

    @Override
    public void create() {
        Arena arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT);
        BulletsContainer bulletsContainer = new BulletsContainer();
        PlayersContainer<RemotePlayer> playersContainer = new PlayersContainer<>();
        Respawner respawner = new Respawner<>(playersContainer, WORLD_WIDTH, WORLD_HEIGHT);
        Collider collider = new Collider<>(playersContainer, bulletsContainer);

        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("HOST", "localhost");
        int port = Integer.parseInt(env.getOrDefault("PORT", "8080"));
        Server server = new SocketIoServer(host, port);

        asteroids = new AsteroidsServerScreen(
                server,
                playersContainer, bulletsContainer,
                arena, respawner, collider);

        setScreen(asteroids);
    }

    @Override
    public void dispose() {
        asteroids.dispose();
    }
}
