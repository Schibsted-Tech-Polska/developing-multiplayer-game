package com.asteroids.game.util;

import java.util.Timer;
import java.util.TimerTask;

public class Delay {
    private final long amount;
    private final Timer timer;

    public Delay(long amount) {
        this(amount, new Timer(true));
    }

    public Delay(long amount, Timer timer) {
        this.amount = amount;
        this.timer = timer;
    }

    public void execute(Runnable task) {
        if(amount == 0) {
            task.run();
            return;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, amount);
    }
}
