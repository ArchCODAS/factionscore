package com.fyzermc.factionscore.command;

import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.util.CountdownTask;
import com.fyzermc.factionscore.util.location.unserializer.BukkitLocationParser;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class FpsCommand implements CommandExecutor {

    public static final Set<String> CACHED = new HashSet<>();

    private static final BukkitLocationParser LOCATION_PARSER = new BukkitLocationParser();

    public FpsCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("fps").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        Location defaultLocation = player.getLocation();

        if (CACHED.remove(player.getName())) {
            player.sendMessage(ChatColor.YELLOW + "Agora os geradores serão visíveis para você!");

            countdown(player, defaultLocation);
            return true;
        }

        CACHED.add(player.getName());

        countdown(player, defaultLocation);

        player.sendMessage(ChatColor.YELLOW + "Agora os geradores serão invisíveis para você!");

        return false;
    }

    private void countdown(Player player, Location location) {
        player.teleport(FactionsCoreConstants.SPAWN.parser(LOCATION_PARSER));

        new CountdownTask(
                3,
                value -> {
                    player.sendMessage(ChatColor.YELLOW + "Retornando em " + value + " segundos.");

                    player.playSound(player.getLocation(), Sound.FIRE_IGNITE, 1F, 1F);
                },
                () -> {
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);
                    player.teleport(location);
                }
        ).runTaskTimer(FactionsCorePlugin.INSTANCE, 20L, 20L);
    }
}