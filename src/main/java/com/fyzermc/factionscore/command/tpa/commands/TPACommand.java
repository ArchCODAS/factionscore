package com.fyzermc.factionscore.command.tpa.commands;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.command.tpa.TPAManager;
import com.fyzermc.factionscore.util.PlayerCooldowns;
import com.fyzermc.factionscore.util.TextComponentUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TPACommand implements CommandExecutor {

    public TPACommand(FactionsCorePlugin plugin) {
        plugin.getCommand("tpa").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player requester = (Player) sender;

        if (args.length != 1) {
            requester.sendMessage(ChatColor.RED + "Uso: /tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            requester.sendMessage(String.format(ChatColor.RED + "O jogador %s não foi encontrado.", args[0]));
            return true;
        }

        if (!target.isOnline()) {
            requester.sendMessage(String.format(ChatColor.RED + "O jogador %s não está online.", args[0]));
            return true;
        }

        if (TPAManager.hasRequest(requester.getName(), target.getName())) {
            requester.sendMessage(ChatColor.RED + "Você já possui uma solicitação de teleporte para este jogador.");
            return true;
        }

        if (target.equals(requester)) {
            requester.sendMessage(ChatColor.RED + "Você não pode enviar uma solicitação de teleporte para você mesmo.");
            return true;
        }

        if (!PlayerCooldowns.hasEnded(requester.getName(), "tpa")) {
            requester.sendMessage(ChatColor.RED + "Aguarde para enviar outra solicitação de teleporte.");
            return true;
        }

        PlayerCooldowns.start(requester.getName(), "tpa", 30, TimeUnit.SECONDS);

        TPAManager.request(target.getName(), requester.getName());

        Bukkit.getScheduler().runTaskLater(FactionsCorePlugin.INSTANCE, () -> TPAManager.remove(target.getName(), requester.getName()), 30 * 20L);

        target.sendMessage(new String[]{
                "",
                "§eVocê recebeu um pedido de teleporte de " + requester.getName() + "§e.",
                ""
        });
        target.sendMessage(TextComponentUtils.getAcceptOrDenyMessage(
                ChatColor.YELLOW, "/tpaccept " + requester.getName(), "/tpdeny " + requester.getName())
        );

        ComponentBuilder builder = new ComponentBuilder("\n")
                .append("Você enviou um pedido de teleporte para " + target.getName()).color(ChatColor.YELLOW)
                .append("\n")
                .append("Clique").color(ChatColor.YELLOW)
                .append(" AQUI").color(ChatColor.DARK_RED).bold(true)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpcancel " + target.getName()))
                .append(" para cancelar.").color(ChatColor.YELLOW).bold(false)
                .append("\n ");

        ((Player) sender).spigot().sendMessage(builder.create());
        return false;
    }
}