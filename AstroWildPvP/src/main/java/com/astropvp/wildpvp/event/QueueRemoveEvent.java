package com.astropvp.wildpvp.event;

import com.astropvp.wildpvp.queue.WildQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QueueRemoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private WildQueue queue;
    /** @deprecated */
    @Deprecated
    private Player host;

    public void setQueue(WildQueue queue) {
        this.queue = queue;
    }

    /** @deprecated */
    @Deprecated
    public void setHost(Player host) {
        this.host = host;
    }

    public WildQueue getQueue() {
        return this.queue;
    }

    /** @deprecated */
    @Deprecated
    public Player getHost() {
        return this.host;
    }

    public QueueRemoveEvent(WildQueue queue) {
        this.host = queue.getHost();
        this.queue = queue;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
