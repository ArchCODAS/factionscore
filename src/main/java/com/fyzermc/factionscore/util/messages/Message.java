package com.fyzermc.factionscore.util.messages;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message extends MessageFormatter<CommandSender> {

    public static Message SUCCESS = new Message("&a") {
        @Override
        public void send(CommandSender sender, String message) {
            super.send(sender, message);

            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
            }
        }
    };

    public static Message ERROR = new Message("&c") {
        @Override
        public void send(CommandSender sender, String message) {
            super.send(sender, message);

            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            }
        }
    };

    public static Message INFO = new Message("&e");
    public static Message EMPTY = new Message("");

    public Message(String prefix) {
        super(prefix);
    }

    @Override
    public void send(CommandSender sender, String message) {
        if (sender != null) {
            sender.sendMessage(getMessage(message));
        }
    }
}