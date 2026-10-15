/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.compilex.CosmicDungeons.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveRegionEvent
extends Event
implements Cancellable {
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String regionName;

    public Player getPlayer() {
        return this.player;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public PlayerLeaveRegionEvent(Player p, String regionName) {
        this.player = p;
        this.regionName = regionName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

