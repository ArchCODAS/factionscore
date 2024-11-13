package com.fyzermc.factionscore.command;

import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.FactionsCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Queue;

public class KickAllCommand implements CommandExecutor {

    public KickAllCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("kickall").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.hasPermission("command.kickall")) {
            sender.sendMessage(ChatColor.RED + "Você não possui permissão para executar este comando.");
            return true;
        }

        Bukkit.setWhitelist(true);

        sender.sendMessage(ChatColor.YELLOW + "Whitelist ligada... Começando a expulsar jogadores.");

        Queue<Player> players = new ArrayDeque<>(Bukkit.getOnlinePlayers());

        players.removeIf(player -> player.getName().equals(sender.getName()));

        new BukkitRunnable() {
            @Override
            public void run() {

                CombatManager.COMBAT.clear();

                Player player = players.poll();
                if (player == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Jogadores expulsos.");
                    this.cancel();
                    return;
                }

                if (player.isOnline()) {
                    player.closeInventory();
                    player.saveData();
                    player.kickPlayer("§cReiniciando.");
                }
            }
        }.runTaskTimer(FactionsCorePlugin.INSTANCE, 13L, 13L);

        return false;
    }
}