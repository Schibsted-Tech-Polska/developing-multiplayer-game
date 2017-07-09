package com.asteroids.game;

import com.asteroids.game.container.Container;
import com.asteroids.game.container.PlayersContainer;
import com.asteroids.game.manager.Collider;
import com.asteroids.game.manager.Respawner;
import com.asteroids.game.model.Arena;
import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.asteroids.game.rendering.ContainerRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AsteroidsScreen extends ScreenAdapter {
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Arena arena;
    private final PlayersContainer playersContainer;
    private final Container<Bullet> bulletsContainer;
    private final Respawner respawner;
    private final Collider collider;
    private final ContainerRenderer<Player> playersRenderer;
    private final ContainerRenderer<Bullet> bulletsRenderer;

    public AsteroidsScreen(
            Viewport viewport, ShapeRenderer shapeRenderer,
            PlayersContainer playersContainer, Container<Bullet> bulletsContainer,
            Arena arena, Respawner respawner, Collider collider,
            ContainerRenderer<Player> playersRenderer, ContainerRenderer<Bullet> bulletsRenderer) {
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.arena = arena;
        this.respawner = respawner;
        this.collider = collider;
        this.playersContainer = playersContainer;
        this.bulletsContainer = bulletsContainer;
        this.playersRenderer = playersRenderer;
        this.bulletsRenderer = bulletsRenderer;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        playersRenderer.render(shapeRenderer);
        bulletsRenderer.render(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
