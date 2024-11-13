package com.fyzermc.factionscore.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import java.util.EnumSet;
import java.util.Set;

public class EntityUtils {

    public static final Set<Material> BLOCKS_NONFULL_LIST = EnumSet.of(
            Material.WALL_SIGN,
            Material.SIGN,
            Material.SIGN_POST,
            Material.WOOD_BUTTON,
            Material.STONE_BUTTON,
            Material.STONE_PLATE,
            Material.WOOD_PLATE,
            Material.FENCE,
            Material.TRAP_DOOR,
            Material.IRON_FENCE,
            Material.THIN_GLASS,
            Material.FENCE_GATE,
            Material.BRICK_STAIRS,
            Material.SMOOTH_STAIRS,
            Material.NETHER_FENCE,
            Material.NETHER_BRICK_STAIRS,
            Material.ENCHANTMENT_TABLE,
            Material.ENDER_PORTAL_FRAME,
            Material.WOOD_STEP,
            Material.SANDSTONE_STAIRS,
            Material.ENDER_CHEST,
            Material.SPRUCE_WOOD_STAIRS,
            Material.BIRCH_WOOD_STAIRS,
            Material.JUNGLE_WOOD_STAIRS,
            Material.COBBLE_WALL,
            Material.TRAPPED_CHEST,
            Material.GOLD_PLATE,
            Material.IRON_PLATE,
            Material.DAYLIGHT_DETECTOR,
            Material.HOPPER,
            Material.QUARTZ_STAIRS,
            Material.STAINED_GLASS_PANE,
            Material.ACACIA_STAIRS,
            Material.DARK_OAK_STAIRS,
            Material.IRON_TRAPDOOR,
            Material.RED_SANDSTONE_STAIRS,
            Material.STONE_SLAB2,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.SPRUCE_FENCE,
            Material.BIRCH_FENCE,
            Material.JUNGLE_FENCE,
            Material.DARK_OAK_FENCE,
            Material.ACACIA_FENCE,
            Material.WOOD_DOOR,
            Material.BED,
            Material.CAULDRON_ITEM,
            Material.BANNER,
            Material.SPRUCE_DOOR_ITEM,
            Material.BIRCH_DOOR_ITEM,
            Material.JUNGLE_DOOR_ITEM,
            Material.ACACIA_DOOR_ITEM,
            Material.DARK_OAK_DOOR_ITEM,
            Material.STEP,
            Material.SOUL_SAND
    );

    public static boolean isStuck(LivingEntity entity) {
        for (int y = entity.getLocation().getBlockY(); y <= entity.getEyeLocation().getBlockY(); y++) {
            Location location = new Location(entity.getWorld(), entity.getLocation().getBlockX(), y, entity.getLocation().getBlockZ());
            Material type = location.getBlock().getType();
            if (type.isSolid() && !BLOCKS_NONFULL_LIST.contains(type)) {
                return true;
            }
        }

        return false;
    }
}