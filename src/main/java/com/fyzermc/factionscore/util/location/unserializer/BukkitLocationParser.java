package com.fyzermc.factionscore.util.location.unserializer;

import com.fyzermc.factionscore.util.location.LocationParser;
import com.fyzermc.factionscore.util.location.SerializedLocation;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@NoArgsConstructor
public class BukkitLocationParser implements LocationParser<Location> {

    public static SerializedLocation serialize(Location location) {
        return new SerializedLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    @Override
    public Location apply(SerializedLocation serialized) {
        return new Location(
                Bukkit.getWorld(serialized.getWorldName()),
                serialized.getX(),
                serialized.getY(),
                serialized.getZ(),
                serialized.getYaw(),
                serialized.getPitch()
        );
    }
}