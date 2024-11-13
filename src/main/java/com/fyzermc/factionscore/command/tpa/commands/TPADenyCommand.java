package com.fyzermc.factionscore.command.tpa.commands;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.command.tpa.TPAManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPADenyCommand implements CommandExecutor {

    public TPADenyCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("tpdeny").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Uso: /tpdeny <nick>");
            return true;
        }

        Player requester = Bukkit.getPlayerExact(args[0]);
        if (requester == null) {
            sender.sendMessage(String.format(ChatColor.RED + "O jogador %s não foi encontrado", args[0]));
            return true;
        }

        if (!requester.isOnline()) {
            sender.sendMessage(String.format(ChatColor.RED + "O jogador %s não está online.", requester.getName()));
            return true;
        }

        Player target = (Player) sender;

        if (!TPAManager.hasRequest(target.getName(), requester.getName())) {
            sender.sendMessage(String.format(ChatColor.RED + "%s não te enviou um pedido de teleporte.", requester.getName()));
            return true;
        }

        TPAManager.remove(target.getName(), requester.getName());

        requester.sendMessage(ChatColor.YELLOW + "Pedido de teleporte para " + target.getName() + " negado.");
        target.sendMessage(String.format(ChatColor.YELLOW + "Você negou o pedido de teleporte de %s", requester.getName()));

        return false;
    }
}