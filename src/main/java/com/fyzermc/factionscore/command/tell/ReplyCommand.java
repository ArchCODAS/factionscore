package com.fyzermc.factionscore.command.tell;

import com.fyzermc.factionscore.FactionsCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class ReplyCommand implements CommandExecutor {

    public static final Map<String, String> REPLYS = new WeakHashMap<>();

    public ReplyCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("reply").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        String rawReplying = REPLYS.get(player.getName());
        if (rawReplying == null) {
            player.sendMessage(ChatColor.RED + "Você não tem ninguém para responder.");
            return true;
        }

        Player replying = Bukkit.getPlayerExact(rawReplying);
        if (replying == null || !replying.isOnline()) {
            player.sendMessage(ChatColor.RED + "Você não tem ninguém para responder.");
            return true;
        }

        String message = String.join(" ", args);

        TellCommand.sendPrivateMessage(player, replying, message);

        return false;
    }
}