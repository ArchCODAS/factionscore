package com.fyzermc.factionscore.misc.blocks;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.Set;

public class FactionsBlocksConstants {

    public static final Set<MaterialData> VALID_MATERIALS = Sets.newHashSetWithExpectedSize(2);

    static {
        VALID_MATERIALS.add(new MaterialData(Material.BEDROCK));
        VALID_MATERIALS.add(new MaterialData(Material.OBSIDIAN));
        VALID_MATERIALS.add(new MaterialData(Material.ENDER_STONE));
    }

}