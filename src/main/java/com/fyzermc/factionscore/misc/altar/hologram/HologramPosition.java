package com.fyzermc.factionscore.misc.altar.hologram;

public enum HologramPosition {
    UP(0.23D);

    private final double value;

    HologramPosition(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}