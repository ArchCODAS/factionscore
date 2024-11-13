package com.fyzermc.factionscore.command.tpa.commands;

import com.fantasy.combatlog.manager.CombatManager;
import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.command.tpa.TPAManager;
import com.fyzermc.factionscore.util.EntityUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TPAcceptCommand implements CommandExecutor {

    public TPAcceptCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("tpaccept").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Uso: /tpaccept <jogador>");
            return true;
        }

        Player target = (Player) sender;
        String requesterName;

        if (args.length == 0) {
            requesterName = TPAManager.getLast(target.getName());

            if (requesterName == null) {
                sender.sendMessage(ChatColor.RED + "Você não possui nenhum pedido de teleporte pendente.");
                return true;
            }

        } else {
            requesterName = args[0];
        }

        Player requester = Bukkit.getPlayerExact(requesterName);
        if (requester == null) {
            sender.sendMessage(String.format(ChatColor.RED + "O jogador %s não foi encontrado.", args[0]));
            return true;
        }

        if (!requester.isOnline()) {
            sender.sendMessage(String.format(ChatColor.RED + "O jogador %s não está online.", requester.getName()));
            return true;
        }

        if (!TPAManager.hasRequest(target.getName(), requester.getName())) {
            sender.sendMessage(ChatColor.RED + "Você não possui um pedido de teleporte deste jogador.");
            return true;
        }

        if (!target.isOp() && target.hasPermission("group.moderador")) {
            sender.sendMessage(ChatColor.RED + "Você é um Moderador e não pode fazer isso.");
            return true;
        }

        if (EntityUtils.isStuck(target)) {
            sender.sendMessage(ChatColor.RED + "Você não pode aceitar TPA estando soterrado.");
            return true;
        }

        TPAManager.remove(target.getName(), requester.getName());

        CombatManager combatManager = new CombatManager(requester);
        if (combatManager.hasCombat()) {
            target.sendMessage(ChatColor.RED + "O jogador está em combate e não pode vir!");
            return true;
        }

        requester.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);

        target.sendMessage(String.format(ChatColor.YELLOW + "Você aceitou o pedido de %s com sucesso.", requester.getName()));
        requester.sendMessage(String.format(ChatColor.YELLOW + "Pedido de teleporte aceito por %s.", target.getName()));

        return false;
    }
}