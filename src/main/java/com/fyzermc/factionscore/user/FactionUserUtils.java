package com.fyzermc.factionscore.user;

import com.fyzermc.factionscore.FactionsCoreProvider;
import org.bukkit.entity.Player;

public class FactionUserUtils {

    public static FactionUser wrap(Player player) {
        return wrap(player.getName());
    }

    public static FactionUser wrap(String player) {
        return FactionsCoreProvider.Cache.Local.USERS.provide().get(player);
    }
}