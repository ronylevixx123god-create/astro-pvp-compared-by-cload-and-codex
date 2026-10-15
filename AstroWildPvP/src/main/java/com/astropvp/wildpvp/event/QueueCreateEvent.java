package com.astropvp.wildpvp.event;

import com.astropvp.wildpvp.queue.WildQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QueueCreateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player host;
    private WildQueue queue;
    private boolean cancelled;

    public void setHost(Player host) {
        this.host = host;
    }

    public void setQueue(WildQueue queue) {
        this.queue = queue;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getHost() {
        return this.host;
    }

    public WildQueue getQueue() {
        return this.queue;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public QueueCreateEvent(Player host, WildQueue queue) {
        this.host = host;
        this.queue = queue;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
