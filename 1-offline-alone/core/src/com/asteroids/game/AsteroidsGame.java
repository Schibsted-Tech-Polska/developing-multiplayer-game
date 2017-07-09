package com.asteroids.game;

import com.asteroids.game.container.BulletsContainer;
import com.asteroids.game.container.Container;
import com.asteroids.game.controls.KeyboardControls;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.asteroids.game.model.Ship;
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
        Player player = new Player(new KeyboardControls(), Color.WHITE);
        Container<Bullet> bulletsContainer = new BulletsContainer();
        player.setShip(new Ship(player));

        PlayerRenderer playerRenderer = new PlayerRenderer(player);
        ContainerRenderer<Bullet> bulletsRenderer = new ContainerRenderer<>(bulletsContainer, VisibleRenderer::new);

        asteroids = new AsteroidsScreen(
                viewport, shapeRenderer,
                arena, player, bulletsContainer,
                playerRenderer, bulletsRenderer);

        setScreen(asteroids);
    }

    @Override
    public void dispose() {
        asteroids.dispose();
    }
}
