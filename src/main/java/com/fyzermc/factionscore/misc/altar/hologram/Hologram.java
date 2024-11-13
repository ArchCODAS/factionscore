package com.fyzermc.factionscore.misc.altar.hologram;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final List<HologramLine> lines = new ArrayList<>();
    private final HologramPosition hologramPosition;

    public Hologram(HologramPosition hologramPosition) {
        this.hologramPosition = hologramPosition;
    }

    public void line(String text) {
        lines.add(new HologramLine().text(text));
    }

    public void spawn(Location location) {
        location.getChunk().load();

        Location hologramLocation = location.clone();

        for (HologramLine hologramLine : lines) {
            hologramLine.spawn(hologramLocation);
            hologramLocation.add(0, this.hologramPosition.getValue(), 0);
        }
    }
}