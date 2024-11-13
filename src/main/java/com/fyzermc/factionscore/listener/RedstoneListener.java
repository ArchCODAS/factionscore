package com.fyzermc.factionscore.listener;

import com.fyzermc.factionscore.util.messages.Message;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class RedstoneListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock.getType() == Material.LEVER || clickedBlock.getType().name().endsWith("BUTTON")) {
            Player player = event.getPlayer();

            Set<Block> wires = getBlocks(clickedBlock.getChunk());

            if (wires.size() > 60) {
                event.setCancelled(true);
                Message.ERROR.send(player, "Há muitas conexões de fios de redstone, quebre algumas por favor.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        Material type = block.getType();

        Player player = event.getPlayer();

        if (type.name().endsWith("RAIL")) {
            event.setCancelled(true);
            Message.ERROR.send(player, "Este item está bloqueado.");
            return;
        }

        if (type == Material.LEVER || type.name().endsWith("BUTTON")) {
            Set<Block> wires = getBlocks(block.getChunk());

            if (wires.size() > 60) {
                event.setCancelled(true);
                Message.ERROR.send(player, "Há muitas conexões de fios de redstone, quebre algumas por favor.");
            }
        }
    }

    private Set<Block> getBlocks(Chunk chunk) {
        Set<Block> blocks = new HashSet<>();

        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(Material.REDSTONE_WIRE);
        net.minecraft.server.v1_8_R3.Chunk chunkHandle = ((CraftChunk) chunk).getHandle();

        for (ChunkSection section : chunkHandle.getSections()) {
            if (section == null || section.a()) continue;

            char[] blockIds = section.getIdArray();
            for (int i = 0; i < blockIds.length; i++) {
                IBlockData blockData = net.minecraft.server.v1_8_R3.Block.d.a(blockIds[i]);
                if (blockData != null && blockData.getBlock() == nmsBlock) {
                    blocks.add(chunk.getBlock(i & 0xf, section.getYPosition() | (i >> 8), (i >> 4) & 0xf));
                }
            }
        }

        return blocks;
    }
}