package com.asteroids.game.container;

import com.asteroids.game.model.Bullet;
import com.asteroids.game.model.Player;
import com.asteroids.game.model.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class PlayersContainer<PlayerType extends Player> implements Container<PlayerType> {
    private final List<PlayerType> players;

    public PlayersContainer(List<PlayerType> players) {
        this.players = players;
    }

    public PlayersContainer() {
        this(new ArrayList<>());
    }

    @Override
    public void add(PlayerType toAdd) {
        players.add(toAdd);
    }

    @Override
    public List<PlayerType> getAll() {
        return players;
    }

    @Override
    public void update(float delta) {
        players.forEach(player -> player.update(delta));
    }

    public Stream<Ship> streamShips() {
        return stream()
                .map(Player::getShip)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<Bullet> obtainAndStreamBullets() {
        return streamShips()
                .map(Ship::obtainBullet)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}
