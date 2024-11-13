package com.fyzermc.factionscore.util;

public final class CoordinateUtils {

    private CoordinateUtils() {
        throw new RuntimeException();
    }

    public static long getBlockKey(final int x, final int y, final int z) {
        return ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27) | ((long) y << 54);
    }
}