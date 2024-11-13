package com.fyzermc.factionscore.misc.chat.listener;

import com.fyzermc.factionscore.misc.chat.ChatManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.setCancelled(true);

        if (!event.getPlayer().hasPermission("chat.toggle") && ChatManager.CHAT_MUTED) {
            event.getPlayer().sendMessage(ChatColor.RED + "O chat local está temporáriamente desabilitado.");
            return;
        }

        ChatManager.buildLocalMessage(
                event.getPlayer(),
                event.getMessage()
        );
    }
}