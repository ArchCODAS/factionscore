package com.fyzermc.factionscore.misc.customitem.command;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.misc.customitem.CustomItem;
import com.fyzermc.factionscore.misc.customitem.CustomItemRegistry;
import com.fyzermc.factionscore.util.messages.Message;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCustomItemCommand implements CommandExecutor {

    public GiveCustomItemCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("givecustomitem").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("customitem.admin")) {
            sender.sendMessage("§cVocê não possui permissão suficiente.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Uso: /givecustomitem <jogador> <item> <quantia>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
            return true;
        }

        CustomItem item = CustomItemRegistry.getItem(args[1].toLowerCase());
        if (item == null) {
            sender.sendMessage("§cItem inválido.");
            return true;
        }

        Integer amount = Ints.tryParse(args[2]);
        if (amount == null || amount < 0) {
            sender.sendMessage("§cQuantia inválida.");
            return true;
        }

        ItemStack itemStack = item.asItemStack(amount);

        target.getInventory().addItem(itemStack);

        Message.SUCCESS.send(sender, "Item enviado com sucesso!");

        return false;
    }
}