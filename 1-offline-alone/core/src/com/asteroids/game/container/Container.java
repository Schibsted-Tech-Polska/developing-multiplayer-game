package com.asteroids.game.container;

import java.util.List;
import java.util.stream.Stream;

public interface Container<Thing> {
    void add(Thing toAdd);
    List<Thing> getAll();
    default Stream<Thing> stream() {
        return getAll().stream();
    }
    void update(float delta);
}
