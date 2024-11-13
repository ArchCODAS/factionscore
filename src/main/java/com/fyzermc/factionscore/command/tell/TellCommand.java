package com.fyzermc.factionscore.command.tell;

import com.fyzermc.factionscore.FactionsCorePlugin;
import com.fyzermc.factionscore.util.messages.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TellCommand implements CommandExecutor {

    public TellCommand(FactionsCorePlugin plugin) {
        plugin.getCommand("tell").setExecutor(this);
    }

    public static void sendPrivateMessage(Player player, Player target, String message) {
        player.sendMessage(MessageUtils.translateColorCodes(
                "&8Mensagem para " + target.getName() + ": &6" + message
        ));

        target.sendMessage(MessageUtils.translateColorCodes(
                "&8Mensagem de " + player.getName() + ": &6" + message
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Uso: /tell <jogador> <mensagem>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || target == player) {
            player.sendMessage(ChatColor.RED + "Usuário inválido.");
            return true;
        }

        ReplyCommand.REPLYS.put(player.getName(), target.getName());
        ReplyCommand.REPLYS.put(target.getName(), player.getName());

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        sendPrivateMessage(player, target, message);

        return false;
    }
}