package com.fyzermc.factionscore.misc.chat;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.user.FactionUserUtils;
import com.fyzermc.factionscore.util.LPUtils;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import com.fyzermc.factionscore.util.messages.MessageUtils;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class ChatManager {

    private static final String COOLDOWN_KEY_GLOBAL = "COOLDOWN_KEY_GLOGAL";
    private static final String COOLDOWN_KEY_LOCAL = "COOLDOWN_KEY_LOCAL";
    private static final double PRICE_TO_TALK = 5000.0;
    public static boolean CHAT_MUTED = false;

    public static void buildLocalMessage(Player player, String rawMessage) {
        boolean isStaff = player.hasPermission("grupo.staff");

        if (!isStaff && !PlayerCooldowns.hasEnded(player.getName(), COOLDOWN_KEY_LOCAL)) {
            player.sendMessage(ChatColor.RED + "Aguarde " + PlayerCooldowns.getSecondsLeft(player.getName(), COOLDOWN_KEY_LOCAL) + " segundos para utilizar o chat novamente.");
            return;
        }

        if (!isStaff && !FactionsCorePlugin.INSTANCE.getEconomy().has(Bukkit.getOfflinePlayer(player.getUniqueId()), PRICE_TO_TALK)) {
            player.sendMessage(ChatColor.RED + "Você precisa de 5.000 coins para utilizar o chat");
            return;
        }

        PlayerCooldowns.start(player.getName(), COOLDOWN_KEY_LOCAL, 3, TimeUnit.SECONDS);

        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.YELLOW)
                .append("[L] ");

        MPlayer mPlayer = FactionUserUtils.wrap(player).getMPlayer();
        Faction faction = mPlayer.getFaction();
        if (faction != null && !faction.isNone()) {
            builder.append(ChatColor.GRAY)
                    .append("[");

            Rel role = mPlayer.getRole();

            builder.append(role.getPrefix())
                    .append(faction.getTag());

            builder.append(ChatColor.GRAY)
                    .append("] ");
        }

        String prefix = MessageUtils.translateColorCodes(LPUtils.getPrimaryGroupPrefix(player));

        builder.append(prefix);

        builder.append(ChatColor.WHITE)
                .append(player.getName())
                .append(ChatColor.YELLOW)
                .append(": ");

        String message = MessageUtils.stripColor(MessageUtils.translateColorCodes(rawMessage));

        if (!message.isEmpty()) {
            builder.append(ChatColor.YELLOW).append(message);

            String messageOut = builder.toString();
            player.sendMessage(messageOut);

            int index = 0;
            for (Entity entity : player.getNearbyEntities(25.0D, 25.0D, 25.0D)) {
                if (entity.getType() == EntityType.PLAYER) {
                    Player entity0 = (Player) entity;
                    if (entity0.hasMetadata("NPC")) {
                        continue;
                    }

                    entity0.sendMessage(messageOut);

                    index++;
                }
            }

            if (index == 0) {
                player.sendMessage(ChatColor.YELLOW + "Não há jogadores perto de você.");
            }
        }
    }

    public static String buildGlobalMessage(Player player, String rawMessage) {
        boolean isStaff = player.hasPermission("grupo.staff");

        if (!isStaff && !PlayerCooldowns.hasEnded(player.getName(), COOLDOWN_KEY_GLOBAL)) {
            player.sendMessage(ChatColor.RED + "Aguarde " + PlayerCooldowns.getSecondsLeft(player.getName(), COOLDOWN_KEY_LOCAL) + " segundos para utilizar o chat novamente.");
            return null;
        }

        if (!isStaff && !FactionsCorePlugin.INSTANCE.getEconomy().has(Bukkit.getOfflinePlayer(player.getUniqueId()), PRICE_TO_TALK)) {
            player.sendMessage(ChatColor.RED + "Você precisa de 5.000 coins para utilizar o chat");
            return null;
        }

        PlayerCooldowns.start(player.getName(), COOLDOWN_KEY_GLOBAL, 5, TimeUnit.SECONDS);

        StringBuilder builder = new StringBuilder();

        boolean isHighlighted = player.hasPermission("chat.highlight");
        if (isHighlighted) {
            builder.append("\n");
        }

        builder.append(ChatColor.GRAY)
                .append("[G] ");

        MPlayer mPlayer = FactionUserUtils.wrap(player).getMPlayer();
        Faction faction = mPlayer.getFaction();
        if (faction != null && !faction.isNone()) {
            builder.append(ChatColor.GRAY)
                    .append("[");

            Rel role = mPlayer.getRole();

            builder.append(role.getPrefix())
                    .append(faction.getTag());

            builder.append(ChatColor.GRAY)
                    .append("] ");
        }

        String prefix = MessageUtils.translateColorCodes(LPUtils.getPrimaryGroupPrefix(player));

        builder.append(prefix);

        builder.append(ChatColor.WHITE)
                .append(player.getName())
                .append(ChatColor.GRAY)
                .append(": ");

        String playerMessage;

        if (isHighlighted) {
            playerMessage = MessageUtils.translateColorCodes(rawMessage);
        } else {
            playerMessage = MessageUtils.stripColor(
                    MessageUtils.translateColorCodes(rawMessage),
                    ChatColor.BLACK,
                    ChatColor.DARK_BLUE,
                    ChatColor.DARK_GRAY,
                    ChatColor.LIGHT_PURPLE,
                    ChatColor.BOLD,
                    ChatColor.UNDERLINE,
                    ChatColor.ITALIC,
                    ChatColor.RESET,
                    ChatColor.MAGIC,
                    ChatColor.STRIKETHROUGH
            );
        }

        if (!player.hasPermission("chat.color")) {
            playerMessage = MessageUtils.stripColor(playerMessage);
        }

        if (!playerMessage.isEmpty()) {
            builder.append(ChatColor.GRAY).append(playerMessage);

            if (isHighlighted) {
                builder.append("\n ");
            }
        }

        return builder.toString();
    }
}