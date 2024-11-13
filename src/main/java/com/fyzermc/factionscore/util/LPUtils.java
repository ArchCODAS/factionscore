package com.fyzermc.factionscore.util;

import com.fyzermc.factionscore.util.messages.MessageUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LPUtils {

    public static Group getPrimaryGroup(String userName) {
        LuckPerms luckPerms = LuckPermsProvider.get();

        return luckPerms.getGroupManager().getGroup(Objects.requireNonNull(luckPerms.getUserManager().getUser(userName)).getPrimaryGroup());
    }

    public static String getPrimaryGroupPrefix(String userName) {
        return MessageUtils.translateColorCodes(getPrimaryGroup(userName).getCachedData().getMetaData().getPrefix());
    }

    public static String getPrimaryGroupPrefix(Player player) {
        return getPrimaryGroupPrefix(player.getName());
    }
}