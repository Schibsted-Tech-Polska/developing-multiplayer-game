package com.asteroids.game.util;

import java.util.List;

public class Randomize {
    public static <Thing> Thing fromList(List<Thing> things) {
        return things.stream()
                .skip((int) (things.size() * Math.random()))
                .findAny()
                .get();
    }
}
