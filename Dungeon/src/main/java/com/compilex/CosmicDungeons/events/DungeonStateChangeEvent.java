/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.compilex.CosmicDungeons.events;

import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DungeonStateChangeEvent
extends Event
implements Cancellable {
    private boolean cancelled;
    private Dungeon dungeon;
    private DungeonState oldState;
    private DungeonState newState;
    private static final HandlerList handlers = new HandlerList();

    public Dungeon getDungeon() {
        return this.dungeon;
    }

    public DungeonState getOldState() {
        return this.oldState;
    }

    public DungeonState getNewState() {
        return this.newState;
    }

    public DungeonStateChangeEvent(Dungeon d, DungeonState state) {
        this.dungeon = d;
        this.oldState = d.getDungeonStatus();
        this.newState = state;
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

