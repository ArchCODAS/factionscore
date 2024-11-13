package com.fyzermc.factionscore.util;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.IntConsumer;

public class CountdownTask extends BukkitRunnable {

    private final IntConsumer countdownAction;
    private final Runnable completionAction;
    private int counter;

    public CountdownTask(final int limit, final IntConsumer countdownAction, final Runnable completionAction) {
        this.counter = limit;
        this.countdownAction = countdownAction;
        this.completionAction = completionAction;
    }

    public void run() {
        if (--this.counter < 0) {
            this.completionAction.run();
            cancel();

            return;
        }

        this.countdownAction.accept(this.counter);
    }
}