package com.fyzermc.factionscore.command;

import com.fyzermc.factionscore.FactionsCoreConstants;
import com.fyzermc.factionscore.FactionsCorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    public ArenaCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("arena").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        player.teleport(FactionsCoreConstants.ARENA.parser(FactionsCoreConstants.LOCATION_PARSER));

        player.sendMessage(ChatColor.YELLOW + "Teletransportado para a localização ARENA com sucesso!");

        return false;
    }
}