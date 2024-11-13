package com.fyzermc.factionscore.misc.genbucket.listeners;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.misc.customitem.events.PlayerUseCustomItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.EventBus;

public class GenBucketListeners implements Listener {

    private PlayerUseCustomItemEvent callPlayerUseCustomItemEvent(Player player, Event triggerEvent, CustomItem item) {
        PlayerUseCustomItemEvent event = new PlayerUseCustomItemEvent(player, triggerEvent, item);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerBucketEmptyEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();

        if (item != null && item.getType() != Material.AIR) {
            CustomItem customItem;
            if ((customItem = CustomItemRegistry.getByItemStack(item)) != null) {
                if (this.callPlayerUseCustomItemEvent(event.getPlayer(), event, customItem).isCancelled()) {
                    event.setCancelled(true);
                } else {
                    EventBus bus = CustomItemRegistry.getEventBus(customItem);
                    bus.post(event);
                }
            }
        }

    }
}