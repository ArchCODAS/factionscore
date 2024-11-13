package com.fyzermc.factionscore.command.vanish.listener;

import com.fyzermc.factionscore.command.vanish.VanishCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        VanishCommand.VANISH_LIST.remove(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (VanishCommand.VANISH_LIST.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (VanishCommand.VANISH_LIST.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (String userName : VanishCommand.VANISH_LIST) {
            Player playerExact = Bukkit.getPlayerExact(userName);
            if (playerExact != null) {
                player.hidePlayer(playerExact);
            }
        }
    }
}