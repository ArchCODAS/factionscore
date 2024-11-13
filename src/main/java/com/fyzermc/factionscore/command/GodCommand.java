package com.fyzermc.factionscore.command;

import com.fyzermc.factionscore.FactionsCorePlugin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GodCommand implements CommandExecutor {

    public GodCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("god").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("system.god")) {
            player.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando");
            return true;
        }

        Player target = null;
        if (args.length > 0) {
            target = Bukkit.getPlayerExact(args[0]);
        }

        if (target != null) {
            EntityPlayer entityPlayer = ((CraftPlayer) target).getHandle();
            entityPlayer.abilities.isInvulnerable = !entityPlayer.abilities.isInvulnerable;

            target.sendMessage(ChatColor.YELLOW + "Modo god " + (entityPlayer.abilities.isInvulnerable ? "habilitado" : "desabilitado") + " com sucesso.");

            return true;
        }

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.abilities.isInvulnerable = !entityPlayer.abilities.isInvulnerable;

        player.sendMessage(ChatColor.YELLOW + "Modo god " + (entityPlayer.abilities.isInvulnerable ? "habilitado" : "desabilitado") + " com sucesso.");

        return false;
    }
}