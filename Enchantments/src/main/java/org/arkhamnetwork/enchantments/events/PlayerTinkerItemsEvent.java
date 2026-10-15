package org.arkhamnetwork.enchantments.events;

import org.arkhamnetwork.enchantments.libs.TinkeredItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PlayerTinkerItemsEvent extends Event implements Cancellable {
    private static HandlerList handlerList;

    static {
        PlayerTinkerItemsEvent.handlerList = new HandlerList();
    }

    private boolean cancelled;
    private Player player;
    private List<TinkeredItem> tinkeredItems;

    public PlayerTinkerItemsEvent(final Player player, final List<TinkeredItem> tinkeredItems) {
        this.player = player;
        this.tinkeredItems = tinkeredItems;
    }

    public PlayerTinkerItemsEvent(final boolean cancelled, final Player player, final List<TinkeredItem> tinkeredItems) {
        this.cancelled = cancelled;
        this.player = player;
        this.tinkeredItems = tinkeredItems;
    }

    public static HandlerList getHandlerList() {
        return PlayerTinkerItemsEvent.handlerList;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerTinkerItemsEvent.handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<TinkeredItem> getTinkeredItems() {
        return this.tinkeredItems;
    }
}
