package com.fyzermc.factionscore.misc.customitem.events;

import com.fyzermc.factionscore.misc.customitem.CustomItem;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUseCustomItemEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Player player;

    @Getter
    private final Event triggerEvent;

    @Getter
    private final CustomItem item;

    private boolean cancelled;

    public PlayerUseCustomItemEvent(Player player, Event triggerEvent, CustomItem item) {
        this.player = player;
        this.triggerEvent = triggerEvent;
        this.item = item;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}