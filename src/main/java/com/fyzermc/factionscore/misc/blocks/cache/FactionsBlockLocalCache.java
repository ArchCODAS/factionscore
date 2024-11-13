package com.fyzermc.factionscore.misc.blocks.cache;

import com.fyzermc.factionscore.misc.blocks.FactionBlock;
import com.fyzermc.factionscore.misc.blocks.utils.FactionsBlockUtils;
import com.fyzermc.factionscore.util.CoordinateUtils;
import com.fyzermc.factionscore.util.cache.LocalCache;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class FactionsBlockLocalCache implements LocalCache {

    private final Map<Long, FactionBlock> blocksMap = new HashMap<>();

    public FactionBlock get(Block block, boolean populate) {
        long blockKey = CoordinateUtils.getBlockKey(block.getX(), block.getY(), block.getZ());

        FactionBlock factionBlock = blocksMap.get(blockKey);
        if (factionBlock == null) {
            double maxDurability = FactionsBlockUtils.getDurabilityByType(block.getType());

            factionBlock = new FactionBlock(
                    maxDurability,
                    maxDurability
            );

            if (populate) {
                blocksMap.put(blockKey, factionBlock);
            }
        }

        return factionBlock;
    }

    public void put(Block block) {
        long blockKey = CoordinateUtils.getBlockKey(block.getX(), block.getY(), block.getZ());

        double durability = 1.0;
        double maxDurability = FactionsBlockUtils.getDurabilityByType(block.getType());

        blocksMap.put(
                blockKey,
                new FactionBlock(Math.round(durability), maxDurability)
        );
    }

    public void remove(Block block) {
        long blockKey = CoordinateUtils.getBlockKey(block.getX(), block.getY(), block.getZ());
        blocksMap.remove(blockKey);
    }

    public void clean() {
        blocksMap.entrySet().removeIf(entry -> {
            FactionBlock factionBlock = entry.getValue();
            return factionBlock.getDurability() >= factionBlock.getMaxDurability();
        });
    }
}