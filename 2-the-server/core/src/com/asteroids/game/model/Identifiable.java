package com.asteroids.game.model;

import java.util.UUID;

public interface Identifiable {
    UUID getId();
    default boolean isIdEqual(UUID otherId) {
        return getId().equals(otherId);
    }
}
