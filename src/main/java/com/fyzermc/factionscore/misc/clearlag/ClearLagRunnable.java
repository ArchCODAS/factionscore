package com.fyzermc.factionscore.misc.clearlag;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.util.CountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ClearLagRunnable implements Runnable {

    @Override
    public void run() {
        new CountdownTask(
                11,
                second -> {
                    if (second > 4) {
                        String message = ("§eLimpando o chão em§f " + second + " §esegundos.");
                        Bukkit.broadcastMessage(message);
                    }
                },
                () -> {

                    int index = 0;

                    for (Entity entity : Bukkit.getWorld("world").getEntities()) {
                        if (entity.getType() == EntityType.DROPPED_ITEM) {
                            entity.remove();

                            index++;
                        }
                    }

                    String message = ("§eForam removidos§f " + index + " §eitens do chão.");
                    Bukkit.broadcastMessage(message);
                }
        ).runTaskTimer(FactionsCorePlugin.INSTANCE, 20L, 20L);
    }
}