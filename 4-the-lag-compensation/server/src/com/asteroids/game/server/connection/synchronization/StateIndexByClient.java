package com.asteroids.game.server.connection.synchronization;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateIndexByClient {
    private final Map<UUID, Long> indexes;

    public StateIndexByClient(Map<UUID, Long> indexes) {
        this.indexes = indexes;
    }

    public StateIndexByClient() {
        this(new HashMap<>());
    }

    public Long lastIndexFor(UUID id) {
        Long sequence = indexes.get(id);
        if(sequence == null) return -1L;
        return sequence;
    }

    public void setIndexFor(UUID id, Long value) {
        indexes.put(id, value);
    }
}
