package com.cosmicpvp.cosmicwildpvp.event;

import com.cosmicpvp.cosmicwildpvp.queue.SingleWildQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class QueueRemoveEvent extends Event
{
    private static final HandlerList handlers;
    private SingleWildQueue queue;
    @Deprecated
    private Player host;

    public QueueRemoveEvent(final SingleWildQueue queue) {
        this.host = queue.getHost();
        this.queue = queue;
    }

    public HandlerList getHandlers() {
        return QueueRemoveEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return QueueRemoveEvent.handlers;
    }

    public SingleWildQueue getQueue() {
        return this.queue;
    }

    @Deprecated
    public Player getHost() {
        return this.host;
    }

    public void setQueue(final SingleWildQueue queue) {
        this.queue = queue;
    }

    @Deprecated
    public void setHost(final Player host) {
        this.host = host;
    }

    static {
        handlers = new HandlerList();
    }
}
