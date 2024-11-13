package com.fyzermc.factionscore.command.tpa.commands;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.command.tpa.TPAManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPACancelCommand implements CommandExecutor {

    public TPACancelCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("tpcancel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Uso: /tpcancel <nick>");
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

        TPAManager.remove(requester.getName(), target.getName());

        target.sendMessage(ChatColor.YELLOW + "Pedido de teleporte cancelado por " + requester.getName() + ".");
        sender.sendMessage(String.format(ChatColor.YELLOW + "Pedido de teleporte para %s cancelado.", target.getName()));

        return false;
    }
}