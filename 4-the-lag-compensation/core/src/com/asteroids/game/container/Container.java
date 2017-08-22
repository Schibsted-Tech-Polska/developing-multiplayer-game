package com.asteroids.game.container;

import com.asteroids.game.model.Identifiable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Container<Thing extends Identifiable> {
    void add(Thing toAdd);
    List<Thing> getAll();
    default Stream<Thing> stream() {
        return getAll().stream();
    }
    void update();
    void move(float delta);
    default Optional<Thing> getById(UUID id) {
        return stream()
                .filter(thing -> thing.isIdEqual(id))
                .findAny();
    }
    default Optional<Thing> getById(String id) {
        return getById(UUID.fromString(id));
    }
    default void removeById(UUID id) {
        getAll().removeIf(thing -> thing.isIdEqual(id));
    }
    default void removeById(String id) {
        removeById(UUID.fromString(id));
    }
}
