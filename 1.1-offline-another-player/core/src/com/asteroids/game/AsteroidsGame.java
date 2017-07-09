package com.asteroids.game;

import com.asteroids.game.container.BulletsContainer;
import com.asteroids.game.container.Container;
import com.asteroids.game.container.PlayersContainer;
import com.asteroids.game.controls.KeyboardControls;
import com.asteroids.game.controls.NoopControls;
import com.asteroids.game.manager.Collider;
import com.asteroids.game.manager.Respawner;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.asteroids.game.rendering.ContainerRenderer;
import com.asteroids.game.rendering.PlayerRenderer;
import com.asteroids.game.rendering.VisibleRenderer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AsteroidsGame extends Game {
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 600f;
    private Screen asteroids;

    @Override
    public void create() {
        Viewport viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT);
        ShapeRenderer shapeRenderer =  new ShapeRenderer();

        Arena arena = new Arena(WORLD_WIDTH, WORLD_HEIGHT);
        Player player1 = new Player(new KeyboardControls(), Color.WHITE);
        Player player2 = new Player(new NoopControls(), Color.LIGHT_GRAY);
        Container<Bullet> bulletsContainer = new BulletsContainer();
        PlayersContainer playersContainer = new PlayersContainer();
        playersContainer.add(player1);
        playersContainer.add(player2);
        Respawner respawner = new Respawner(playersContainer, WORLD_WIDTH, WORLD_HEIGHT);
        Collider collider = new Collider(playersContainer, bulletsContainer);

        ContainerRenderer<Bullet> bulletsRenderer = new ContainerRenderer<>(bulletsContainer, VisibleRenderer::new);
        ContainerRenderer<Player> playersRenderer = new ContainerRenderer<>(playersContainer, PlayerRenderer::new);

        asteroids = new AsteroidsScreen(
                viewport, shapeRenderer,
                playersContainer, bulletsContainer,
                arena, respawner, collider,
                playersRenderer, bulletsRenderer);

        setScreen(asteroids);
    }

    @Override
    public void dispose() {
        asteroids.dispose();
    }
}
