/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.astropvp.core.killtracker.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSubtractSoulsEvent
extends Event
implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private int soulsToSubtract;

    public PlayerSubtractSoulsEvent(Player player, int soulsToSubtract) {
        this.player = player;
        this.soulsToSubtract = soulsToSubtract;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getSoulsToSubtract() {
        return this.soulsToSubtract;
    }

    public void setSoulsToSubtract(int soulsToSubtract) {
        this.soulsToSubtract = soulsToSubtract;
    }
}

