package com.fyzermc.factionscore.command.vanish;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class VanishCommand implements CommandExecutor {

    public static final Set<String> VANISH_LIST = Sets.newHashSet();

    public VanishCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("vanish").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Você não pode utilizar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("system.vanish")) {
            player.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando.");
            return true;
        }

        if (VANISH_LIST.remove(player.getName())) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(player);
            }

            player.sendMessage(ChatColor.YELLOW + "Modo invísivel desabilitado com sucesso.");
            return true;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("system.vanish.bypass")) {
                continue;
            }

            onlinePlayer.hidePlayer(player);
        }

        VANISH_LIST.add(player.getName());

        player.sendMessage(ChatColor.YELLOW + "Modo invísivel habilitado com sucesso.");

        return false;
    }
}