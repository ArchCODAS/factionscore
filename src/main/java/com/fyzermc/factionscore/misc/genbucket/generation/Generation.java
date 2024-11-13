package com.fyzermc.factionscore.misc.genbucket.generation;

import com.fyzermc.factionscore.FactionsCoreProvider;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Generation {

    private final Block block;
    private int index = 1;
    private boolean completed;

    public Generation(Block block) {
        this.block = block;
    }

    public void generate() {
        Block toGenerate = this.getBlock().getLocation().add(new Vector(0, -this.getIndex(), 0)).getBlock();
        Block belowToGenerate = toGenerate.getLocation().add(new Vector(0, -1, 0)).getBlock();

        this.setIndex(this.getIndex() + 1);
        if (belowToGenerate.getY() > 0 && belowToGenerate.getType() == Material.AIR) {
            this.runBlockUpdate(toGenerate);
        } else {
            this.runBlockUpdate(toGenerate);
            this.setCompleted(true);
        }

        FactionsCoreProvider.Cache.Local.BLOCKS.provide().get(toGenerate, true);
    }

    private void runBlockUpdate(Block block) {
        block.setType(Material.BEDROCK);
    }

    public Block getBlock() {
        return this.block;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}