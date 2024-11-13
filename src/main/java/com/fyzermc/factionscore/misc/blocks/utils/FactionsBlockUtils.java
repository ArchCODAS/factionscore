package com.fyzermc.factionscore.misc.blocks.utils;

import org.bukkit.Material;

public class FactionsBlockUtils {

    public static double getDurabilityByType(Material material) {
        switch (material) {
            case BEDROCK:
                return 10.0;
            case ENDER_STONE:
                return 7.0;
            case OBSIDIAN:
                return 3.0;
            default:
                return 1.0;
        }
    }
}