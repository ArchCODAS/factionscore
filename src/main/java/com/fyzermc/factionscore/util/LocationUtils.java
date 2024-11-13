package com.fyzermc.factionscore.util;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class LocationUtils {

    public static boolean isOutsideBorder(Location location) {
        CraftWorld craftWorld = (CraftWorld) location.getWorld();
        return !craftWorld.getHandle().getWorldBorder().a(new BlockPosition(location.getX(), location.getY(), location.getZ()));
    }
}