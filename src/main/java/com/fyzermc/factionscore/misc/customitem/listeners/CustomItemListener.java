package com.fyzermc.factionscore.misc.customitem.listeners;

import com.fyzermc.factionscore.misc.customitem.CraftableCustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.misc.customitem.events.PlayerUseCustomItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.EventBus;

public class CustomItemListener implements Listener {

    private boolean callPlayerUseCustomItemEvent(Player player, Event triggerEvent, CustomItem item) {

        if (item.getNonUseEvents().contains(triggerEvent.getClass())) {
            return false;
        }

        PlayerUseCustomItemEvent event = new PlayerUseCustomItemEvent(player, triggerEvent, item);

        Bukkit.getServer().getPluginManager().callEvent(event);

        return event.isCancelled();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        for (ItemStack stack : event.getInventory().getContents()) {

            CustomItem customItem = CustomItemRegistry.getByItemStack(stack);

            if (customItem != null && !(customItem instanceof CraftableCustomItem)) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack item = event.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        CustomItem customItem;

        if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
            return;
        }

        EventBus bus = CustomItemRegistry.getEventBus(customItem);

        if (bus.hasSubscriberForEvent(event.getClass())) {

            if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                event.setCancelled(true);
                return;
            }

            bus.post(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        CustomItem customItem;

        if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
            return;
        }

        EventBus bus = CustomItemRegistry.getEventBus(customItem);

        if (bus.hasSubscriberForEvent(event.getClass())) {

            if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                event.setCancelled(true);
                return;
            }

            bus.post(event);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        CustomItem customItem;

        if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
            return;
        }

        EventBus bus = CustomItemRegistry.getEventBus(customItem);

        if (bus.hasSubscriberForEvent(event.getClass())) {

            if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                event.setCancelled(true);
                return;
            }

            bus.post(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().hasMetadata("NPC")) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        CustomItem customItem;

        if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
            return;
        }

        EventBus bus = CustomItemRegistry.getEventBus(customItem);

        if (bus.hasSubscriberForEvent(event.getClass())) {

            if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                event.setCancelled(true);
                return;
            }

            bus.post(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        CustomItem customItem;

        if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
            return;
        }

        EventBus bus = CustomItemRegistry.getEventBus(customItem);

        if (bus.hasSubscriberForEvent(event.getClass())) {

            if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                event.setCancelled(true);
                return;
            }

            bus.post(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        CURRENT_ITEM:
        {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) {
                break CURRENT_ITEM;
            }

            CustomItem customItem;

            if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
                break CURRENT_ITEM;
            }

            EventBus bus = CustomItemRegistry.getEventBus(customItem);

            if (bus.hasSubscriberForEvent(event.getClass())) {

                if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                    event.setCancelled(true);
                    return;
                }

                bus.post(event);
            }
        }

        CURSOR_ITEM:
        {
            ItemStack item = event.getCursor();
            if (item == null || item.getType() == Material.AIR) {
                break CURSOR_ITEM;
            }

            CustomItem customItem;

            if ((customItem = CustomItemRegistry.getByItemStack(item)) == null) {
                break CURSOR_ITEM;
            }

            EventBus bus = CustomItemRegistry.getEventBus(customItem);

            if (bus.hasSubscriberForEvent(event.getClass())) {

                if (callPlayerUseCustomItemEvent(player, event, customItem)) {
                    event.setCancelled(true);
                    return;
                }

                bus.post(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile entity = event.getEntity();

        if (!(entity.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) entity.getShooter();

        ItemStack itemStack = player.getInventory().getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        CustomItem item = CustomItemRegistry.getByItemStack(itemStack);
        if (item == null) {
            return;
        }

        EventBus eventBus = CustomItemRegistry.getEventBus(item);

        if (eventBus.hasSubscriberForEvent(event.getClass())) {
            if (this.callPlayerUseCustomItemEvent(player, event, item)) {
                event.setCancelled(true);
                return;
            }

            eventBus.post(event);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

        Projectile entity = event.getEntity();
        if (!(entity.getShooter() instanceof Player)) {
            return;
        }

        if (!entity.hasMetadata("custom_item_key")) {
            return;
        }

        String custom_item_key = entity.getMetadata("custom_item_key").get(0).asString();

        CustomItem item = CustomItemRegistry.getItem(custom_item_key);
        if (item == null) {
            return;
        }

        Player shooter = (Player) entity.getShooter();

        EventBus eventBus = CustomItemRegistry.getEventBus(item);

        if (eventBus.hasSubscriberForEvent(event.getClass())) {
            if (this.callPlayerUseCustomItemEvent(shooter, event, item)) {
                return;
            }

            eventBus.post(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerFishEvent event) {
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        CustomItem byItemStack = CustomItemRegistry.getByItemStack(item);
        if (byItemStack == null) {
            return;
        }

        if (this.callPlayerUseCustomItemEvent(player, event, byItemStack)) {
            event.setCancelled(true);
            return;
        }

        EventBus bus = CustomItemRegistry.getEventBus(byItemStack);
        bus.post(event);
    }
}