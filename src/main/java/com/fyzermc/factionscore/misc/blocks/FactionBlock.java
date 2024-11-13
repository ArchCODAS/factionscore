package com.fyzermc.factionscore.misc.blocks;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class FactionBlock {

    private double durability;

    @Setter
    private double maxDurability;

    @Setter
    private long lastRegen;

    public FactionBlock(double durability, double maxDurability) {
        this.durability = durability;
        this.maxDurability = maxDurability;
        this.lastRegen = System.currentTimeMillis();
    }

    public double getDurability() {
        regen();

        return durability;
    }

    public void damage(double damage) {
        this.durability = this.durability - damage;
    }

    private void regen() {
        if (!canRegen()) {
            return;
        }

        long lastHealthUpdateDif = System.currentTimeMillis() - lastRegen;

        long minutes = (lastHealthUpdateDif / 1000) / 60;

        if (minutes >= 1) {
            if (this.durability + minutes <= maxDurability) {
                this.durability += minutes;
            }
        }

        this.lastRegen = System.currentTimeMillis();
    }

    private boolean canRegen() {
        long lastHitDif = System.currentTimeMillis() - this.lastRegen;
        long lastHitDifSeconds = lastHitDif / 1000;

        long minutes = lastHitDifSeconds / 60;

        return minutes >= 1 && durability != maxDurability;
    }

    public boolean isDead() {
        return durability <= 0;
    }

    public double getMaxDurability() {
        return maxDurability;
    }
}