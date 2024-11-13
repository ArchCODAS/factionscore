package com.fyzermc.factionscore.misc.altar.commands;

import com.fyzermc.factionscore.FactionsCorePlugin;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RemoveAltarsCommand implements CommandExecutor {

    public RemoveAltarsCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("removealtars").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("altar.admin")) {
            player.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando");
            return true;
        }

        World world = Bukkit.getWorld("world");
        for (Entity entity : world.getEntities()) {
            if (entity instanceof EnderCrystal || entity instanceof ArmorStand) {
                entity.remove();
            } else if (entity instanceof EntityEnderCrystal) {
                entity.remove();
            }
        }

        player.sendMessage(ChatColor.YELLOW + "Todos os altares foram removidos com sucesso");

        return false;
    }
}