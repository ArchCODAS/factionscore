package com.fyzermc.factionscore.misc.genbucket.task;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.genbucket.generation.Generation;
import org.bukkit.Bukkit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GenerationTask implements Runnable {

    private static final List<Generation> GENS = new LinkedList<>();
    private Integer TASK_ID;

    private void runTask() {
        if (TASK_ID != null) {
            return;
        }

        TASK_ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                FactionsCorePlugin.INSTANCE,
                this,
                15L,
                15L
        );
    }

    public void haltTask() {
        Bukkit.getServer().getScheduler().cancelTask(TASK_ID);
        TASK_ID = null;
    }

    @Override
    public void run() {
        if (GENS.isEmpty()) {
            this.haltTask();
            return;
        }

        Iterator<Generation> iterator = GENS.iterator();
        while (iterator.hasNext()) {
            Generation generation = iterator.next();
            if (generation.isCompleted()) {
                iterator.remove();
                continue;
            }

            generation.generate();
        }

    }

    public void addGeneration(Generation generation) {
        GENS.add(generation);

        runTask();
    }
}