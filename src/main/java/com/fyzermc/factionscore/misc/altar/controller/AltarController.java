package com.fyzermc.factionscore.misc.altar.controller;

import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.altar.AltarConstant;
import com.fyzermc.factionscore.misc.altar.entity.AltarEntity;
import com.fyzermc.factionscore.misc.altar.hologram.Hologram;
import com.fyzermc.factionscore.misc.altar.hologram.HologramPosition;
import com.fyzermc.factionscore.util.NMS;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;

public class AltarController {

    public static boolean SPAWNED = false;

    public static void scheduleSpawn() {
        Bukkit.getScheduler().runTaskLater(FactionsCorePlugin.INSTANCE, () -> {

            if (SPAWNED) {
                System.out.println("Altar já spawnado.");
                return;
            }

            Location location = AltarConstant.LOCATION.parser(FactionsCoreConstants.LOCATION_PARSER);

            int i = 0;

            for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location, 4.0, 4.0, 4.0)) {
                if (nearbyEntity.getType() == EntityType.ENDER_CRYSTAL) {
                    ++i;
                }
            }

            if (i > 0) {
                System.out.println("Já existem altares spawnados.");
                return;
            }

            if (!location.getChunk().isLoaded()) {
                location.getChunk().load();
            }

            SPAWNED = true;

            WorldServer world = NMS.getWorld(location.getWorld());

            Hologram hologram = new Hologram(HologramPosition.UP);
            hologram.line("&dAltar Místico");
            hologram.spawn(location.clone().add(0.0D, 2.3D, 0.0D));

            AltarEntity altarEntity = new AltarEntity(world);
            altarEntity.setPosition(location.getX(), location.getY(), location.getZ());
            world.addEntity(altarEntity);

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§l[ALTAR] §fUm altar místico foi reencarnado na arena!");
            Bukkit.broadcastMessage("");

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENDERMAN_DEATH, 6.0F, -6.0F);
            }

        }, 60 * 1200L);
    }
}