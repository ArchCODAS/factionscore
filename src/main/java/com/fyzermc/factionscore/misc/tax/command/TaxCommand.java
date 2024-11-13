package com.fyzermc.factionscore.misc.tax.command;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.tax.TaxUtil;
import com.fyzermc.factionscore.util.NumberUtils;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TaxCommand implements CommandExecutor {

    public TaxCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("tax").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("command.tax")) {
            player.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Uso: /taxar <tag> <motivo>");
            return true;
        }

        Faction faction = FactionColl.get().getByTag(args[0]);
        if (faction == null) {
            player.sendMessage(ChatColor.RED + "Facção inválida.");
            return true;
        }

        String reason = args[1];

        TaxUtil.tax(faction, value -> Bukkit.broadcastMessage("\n§c* A facção §7[" + faction.getTag() + "] " + faction.getName() +
                " §cfoi taxada em §725%§c de sua fortuna em coins§c §7("
                + NumberUtils.format(value) + ")§c pelo motivo de §7" + reason + "§c.\n "));

        return false;
    }
}