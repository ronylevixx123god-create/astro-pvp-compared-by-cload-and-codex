package org.arkhamnetwork.enchantments.events;

import org.arkhamnetwork.enchantments.libs.TinkeredItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAttemptTinkerItem extends Event implements Cancellable {
    private static HandlerList handlerList;

    static {
        PlayerAttemptTinkerItem.handlerList = new HandlerList();
    }

    private boolean cancelled;
    private Player player;
    private TinkeredItem item;

    public PlayerAttemptTinkerItem(final Player player, final TinkeredItem item) {
        this.player = player;
        this.item = item;
    }

    public PlayerAttemptTinkerItem(final boolean cancelled, final Player player, final TinkeredItem item) {
        this.cancelled = cancelled;
        this.player = player;
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return PlayerAttemptTinkerItem.handlerList;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return PlayerAttemptTinkerItem.handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public TinkeredItem getItem() {
        return this.item;
    }

}
