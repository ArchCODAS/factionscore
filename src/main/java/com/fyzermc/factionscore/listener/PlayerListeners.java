package com.fyzermc.factionscore.listener;

import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.util.EntityUtils;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PlayerListeners implements Listener {

    private final Set<InventoryType> BLOCKED_INV_TYPE = EnumSet.of(
            InventoryType.ANVIL,
            InventoryType.BREWING,
            InventoryType.MERCHANT,
            InventoryType.HOPPER,
            InventoryType.DROPPER,
            InventoryType.ENCHANTING,
            InventoryType.FURNACE
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);

        player.teleport(FactionsCoreConstants.SPAWN.parser(FactionsCoreConstants.LOCATION_PARSER));

        if  (player.hasPermission("join.spectator")) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInitialSpawn(PlayerInitialSpawnEvent event) {
        event.setSpawnLocation(FactionsCoreConstants.SPAWN.parser(FactionsCoreConstants.LOCATION_PARSER));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata(FactionsCoreConstants.METADATA_FALL_BYPASS_KEY)) {
            event.setCancelled(true);
            event.setLeaveMessage(null);
            event.setReason(null);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getEntity().hasMetadata(FactionsCoreConstants.METADATA_FALL_BYPASS_KEY)) {
            event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FALL);
            event.getEntity().removeMetadata(FactionsCoreConstants.METADATA_FALL_BYPASS_KEY, FactionsCorePlugin.INSTANCE);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        event.setCancelled(BLOCKED_INV_TYPE.contains(event.getInventory().getType()));
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        BlockState state = event.getBlock().getState();
        MaterialData data = state.getData();

        if (data instanceof NetherWarts) {
            NetherWarts netherWarts = (NetherWarts) data;
            if (netherWarts.getState() != NetherWartsState.RIPE) {
                event.setCancelled(true);
                return;
            }

            if (netherWarts.getState() == NetherWartsState.RIPE) {
                event.setCancelled(true);
                netherWarts.setState(NetherWartsState.SEEDED);
                state.setData(netherWarts);
                state.update();
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("grupo.staff")) {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR && event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            event.setCancelled(true);
            player.setSpectatorTarget(null);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item == null) {
            return;
        }

        switch (item.getType()) {
            case SADDLE:
            case REDSTONE_COMPARATOR:
            case BANNER: {

                if (!event.isCancelled()) {
                    event.setCancelled(!player.isOp());

                    if (event.isCancelled()) {
                        player.sendMessage(ChatColor.RED + "Não é possível utilizar este item.");
                    }
                }
                break;
            }

            case BOAT: {
                if (!event.isCancelled()) {
                    event.setCancelled(!player.isOp());
                }
                break;
            }

            case ENDER_PEARL: {
                CombatManager combatController = new CombatManager(player);
                if (combatController.hasCombat()) {
                    event.setCancelled(true);
                    return;
                }

                if (!PlayerCooldowns.hasEnded(player.getName(), "enderpearl")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "Você precisa esperar para jogar outra pérola do fim.");
                    return;
                }

                if (!event.isCancelled()) {
                    event.setCancelled(EntityUtils.isStuck(player));

                    if (event.isCancelled()) {
                        player.sendMessage(ChatColor.RED + "Você não pode jogar uma pérola do fim estando dentro de um bloco.");
                        return;
                    }
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    PlayerCooldowns.start(player.getName(), "enderpearl", 5, TimeUnit.SECONDS);
                }
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(FactionsCoreConstants.SPAWN.parser(FactionsCoreConstants.LOCATION_PARSER));
    }
}