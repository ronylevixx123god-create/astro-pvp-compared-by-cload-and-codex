package com.cosmicpvp.cosmicwildpvp.event;

import com.cosmicpvp.cosmicwildpvp.queue.SingleWildQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class QueueCreateEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private Player host;
    private SingleWildQueue queue;
    private boolean cancelled;

    public QueueCreateEvent(final Player host, final SingleWildQueue queue) {
        this.host = host;
        this.queue = queue;
    }

    public HandlerList getHandlers() {
        return QueueCreateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return QueueCreateEvent.handlers;
    }

    public Player getHost() {
        return this.host;
    }

    public SingleWildQueue getQueue() {
        return this.queue;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setHost(final Player host) {
        this.host = host;
    }

    public void setQueue(final SingleWildQueue queue) {
        this.queue = queue;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    static {
        handlers = new HandlerList();
    }
}
