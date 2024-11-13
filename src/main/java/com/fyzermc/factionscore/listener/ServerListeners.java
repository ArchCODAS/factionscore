package com.fyzermc.factionscore.listener;

import com.fyzermc.factionscore.util.LocationUtils;
import net.minecraft.server.v1_8_R3.EntityMonster;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.EnumSet;
import java.util.Set;

public class ServerListeners implements Listener {

    private final Set<CreatureSpawnEvent.SpawnReason> WHITELISTED_REASONS = EnumSet.of(
            CreatureSpawnEvent.SpawnReason.CUSTOM,
            CreatureSpawnEvent.SpawnReason.SPAWNER,
            CreatureSpawnEvent.SpawnReason.SPAWNER_EGG,
            CreatureSpawnEvent.SpawnReason.EGG
    );

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (WHITELISTED_REASONS.contains(event.getSpawnReason())) {
            return;
        }

        event.setCancelled(true);
        event.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setCancelled(true);
            event.setDamage(0.0);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (LocationUtils.isOutsideBorder(event.getTo())) {
            event.setCancelled(true);
            event.setTo(event.getFrom());
        }
    }
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK && event.getTo() == Material.AIR) {
            Material type = event.getBlock().getType();
            if (type == Material.SAND || type == Material.GRAVEL || type == Material.ANVIL) {
                event.setCancelled(true);
                event.getBlock().getState().update(false, false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPistonExtend(BlockPistonRetractEvent event) {
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleCreate(VehicleCreateEvent event) {
        event.getVehicle().remove();
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        event.getVehicle().remove();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityMount(EntityMountEvent event) {
        event.setCancelled(true);

        event.getMount().remove();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.MERCHANT) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(BlockFromToEvent event) {
        Material material = event.getBlock().getType();

        if (material.equals(Material.LAVA) || material.equals(Material.STATIONARY_LAVA)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}
