package com.fyzermc.factionscore.util;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class WorldCuboid implements Cloneable, Iterable<Block> {

    private final String worldName;

    private final int minX;
    private final int minY;
    private final int minZ;

    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public WorldCuboid(String worldName, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.worldName = worldName;

        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public WorldCuboid(Location min, Location max, World world) {
        this(world.getName(), min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(worldName);
    }

    public void getBlocks(Consumer<Block> callback) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    callback.accept(getBukkitWorld().getBlockAt(x, y, z));
                }
            }
        }
    }

    public void getWalls(final Consumer<Block> callback) {
        World world = getBukkitWorld();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                callback.accept(world.getBlockAt(x, y, minZ));

                if (minZ != maxZ) {
                    callback.accept(world.getBlockAt(x, y, maxZ));
                }
            }
        }

        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                callback.accept(world.getBlockAt(minX, y, z));

                if (minX != maxX) {
                    callback.accept(world.getBlockAt(maxX, y, z));
                }
            }
        }
    }

    public void getRoof(final Consumer<Block> callBack) {
        getBlocks(block -> {
            if (block.getLocation().getBlockY() == maxY) {
                callBack.accept(block);
            }
        });
    }

    public void getFloor(final Consumer<Block> callBack) {
        getBlocks(block -> {
            if (block.getLocation().getBlockY() == minY) {
                callBack.accept(block);
            }
        });
    }

    public boolean contains(int x, int y, int z) {
        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }

    public boolean contains(Location location, boolean sameWorld) {
        if (location == null) {
            return false;
        }

        if (sameWorld && !location.getWorld().getName().equals(worldName)) {
            return false;
        }

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        return contains(x, y, z);
    }

    @Override
    public WorldCuboid clone() {
        return new WorldCuboid(worldName, minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public @NonNull Iterator<Block> iterator() {
        return new Iterator<Block>() {

            private int nextX = minX;
            private int nextY = minY;
            private int nextZ = minZ;

            @Override
            public boolean hasNext() {
                return nextX != Integer.MIN_VALUE;
            }

            @Override
            public Block next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Block block = getBukkitWorld().getBlockAt(nextX, nextY, nextZ);
                if (++nextX > maxX) {
                    nextX = minX;
                    if (++nextY > maxY) {
                        nextY = minY;
                        if (++nextZ > maxZ) {
                            nextX = Integer.MIN_VALUE;
                        }
                    }
                }

                return block;
            }
        };
    }
}