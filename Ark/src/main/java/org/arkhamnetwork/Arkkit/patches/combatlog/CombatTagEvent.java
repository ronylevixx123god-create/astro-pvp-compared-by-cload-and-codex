/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package org.arkhamnetwork.Arkkit.patches.combatlog;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatTagEvent
        extends Event
        implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player combatPlayer;
    private final boolean alreadyInCombat;
    private boolean cancelled;

    public CombatTagEvent(Player p, boolean alreadyInCombat) {
        this.combatPlayer = p;
        this.alreadyInCombat = alreadyInCombat;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.combatPlayer;
    }

    public boolean wasAlreadyInCombat() {
        return this.alreadyInCombat;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

