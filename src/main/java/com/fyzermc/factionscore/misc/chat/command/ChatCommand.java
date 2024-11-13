package com.fyzermc.factionscore.misc.chat.command;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.chat.ChatManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements CommandExecutor {

    public ChatCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("chat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("chat.toggle")) {
            player.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando.");
            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            if (!ChatManager.CHAT_MUTED) {
                player.sendMessage(ChatColor.RED + "O chat já está desmutado.");
                return true;
            }

            ChatManager.CHAT_MUTED = false;
            player.sendMessage(ChatColor.GREEN + "O chat foi ativo com sucesso.");
        } else if (args[0].equalsIgnoreCase("off")) {
            if (ChatManager.CHAT_MUTED) {
                player.sendMessage(ChatColor.RED + "O chat já está mutado.");
                return true;
            }

            ChatManager.CHAT_MUTED = true;
            player.sendMessage(ChatColor.GREEN + "O chat foi desativado com sucesso.");
        } else {
            player.sendMessage(ChatColor.RED + "Uso: /chat <on/off>");
        }

        return false;
    }
}