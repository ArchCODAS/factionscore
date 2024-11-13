package com.fyzermc.factionscore.command.visibility;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class ShowCommand implements CommandExecutor {

    public ShowCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("show").setExecutor(this);
    }

    private static final String COOLDOWN_KEY = "show-command";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        if (!PlayerCooldowns.hasEnded(player.getName(), COOLDOWN_KEY)) {
            player.sendMessage(ChatColor.RED + "Aguarde para mostrar os jogadores novamente.");
            return true;
        }

        PlayerCooldowns.start(player.getName(), COOLDOWN_KEY, 3, TimeUnit.SECONDS);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            player.showPlayer(onlinePlayer);
        }

        player.sendMessage(ChatColor.GREEN + "Agora todos os jogadores estão visíveis para você!");

        return false;
    }
}