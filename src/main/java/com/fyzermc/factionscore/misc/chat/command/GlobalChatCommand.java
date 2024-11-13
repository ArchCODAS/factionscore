package com.fyzermc.factionscore.misc.chat.command;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.chat.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GlobalChatCommand implements CommandExecutor {

    public GlobalChatCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("global").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Uso: /g <mensagem>");
            return true;
        }

        if (!player.hasPermission("chat.toggle") && ChatManager.CHAT_MUTED) {
            player.sendMessage(ChatColor.RED + "O chat global está temporáriamente desabilitado.");
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        if (message.toLowerCase().contains("${jndi:ldap://")) {
            player.sendMessage(ChatColor.RED + "Mensagem contém conteúdo inválido.");
            return true;
        }

        String globalMessage = ChatManager.buildGlobalMessage(player, message);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(globalMessage);
        }

        return false;
    }
}