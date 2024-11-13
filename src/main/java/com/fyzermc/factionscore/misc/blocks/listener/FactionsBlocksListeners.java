package com.fyzermc.factionscore.misc.blocks.listener;

import com.fyzermc.factionscore.FactionsCoreProvider;
import com.fyzermc.factionscore.misc.blocks.FactionBlock;
import com.fyzermc.factionscore.misc.blocks.FactionsBlocksConstants;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class FactionsBlocksListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!FactionsBlocksConstants.VALID_MATERIALS.contains(new MaterialData(event.getBlock().getType()))) {
            return;
        }

        FactionsCoreProvider.Cache.Local.BLOCKS.provide().put(event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!FactionsBlocksConstants.VALID_MATERIALS.contains(new MaterialData(event.getBlock().getType()))) {
            return;
        }

        FactionsCoreProvider.Cache.Local.BLOCKS.provide().remove(event.getBlock());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock() || !event.hasItem()) {
            return;
        }

        if (event.getItem().getType() == Material.STICK) {
            Block clickedBlock = event.getClickedBlock();

            if (!FactionsBlocksConstants.VALID_MATERIALS.contains(new MaterialData(clickedBlock.getType()))) {
                return;
            }

            event.setCancelled(true);

            FactionBlock factionBlock = FactionsCoreProvider.Cache.Local.BLOCKS.provide().get(clickedBlock, false);

            Player player = event.getPlayer();

            showDurability(player, factionBlock);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity().hasMetadata("ImpulseTnT")) {
            event.setCancelled(true);
            return;
        }

        Chunk chunk = event.getEntity().getLocation().getChunk();

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(chunk));

        if (!factionAt.isNone() && !factionAt.isInAttack() && factionAt.getOnlinePlayers().isEmpty()) {
            return;
        }

        computeExplosion(event);
    }

    private void computeExplosion(EntityExplodeEvent event) {
        double damage = 1.0;

        if (event.getEntityType() == EntityType.CREEPER) {
            Creeper creeper = (Creeper) event.getEntity();
            damage = creeper.isPowered() ? 5.0 : 2.0;
        }

        Location origin = event.getLocation();

        int radius = 3;
        int radiusSquared = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {

                    Vector vector = new Vector(x, y, z);

                    if (vector.lengthSquared() <= radiusSquared) {
                        Block block = origin.clone().add(vector).getBlock();

                        Material blockType = block.getType();
                        if (blockType == Material.AIR) {
                            continue;
                        }

                        if (block.getY() <= 256 && block.getY() > 0) {
                            if (blockType == Material.MOB_SPAWNER) {
                                continue;
                            }

                            if (FactionsBlocksConstants.VALID_MATERIALS.contains(new MaterialData(blockType))) {
                                FactionBlock factionBlock = FactionsCoreProvider.Cache.Local.BLOCKS.provide().get(block, true);
                                factionBlock.damage(damage);

                                if (factionBlock.isDead()) {
                                    FactionsCoreProvider.Cache.Local.BLOCKS.provide().remove(block);
                                    block.setType(Material.AIR);
                                }

                                continue;
                            }

                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }

    private void showDurability(Player player, FactionBlock block) {
        double durability = block.getDurability();
        double maxDurability = block.getMaxDurability();

        player.sendMessage("Durabilidade: §f" + durability + "/" + maxDurability);
    }
}