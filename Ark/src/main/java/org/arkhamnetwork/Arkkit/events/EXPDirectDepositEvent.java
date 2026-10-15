/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package org.arkhamnetwork.Arkkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EXPDirectDepositEvent
        extends Event
        implements Cancellable {
    public static final HandlerList list = new HandlerList();
    private final Player player;
    private boolean cancelled = false;
    private int amountToGive;
    private int previousEXPTotal;

    public EXPDirectDepositEvent(Player player, int amount, int previousEXP) {
        this.amountToGive = amount;
        this.previousEXPTotal = previousEXP;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    public HandlerList getHandlers() {
        return list;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public int getAmountToGive() {
        return this.amountToGive;
    }

    public void setAmountToGive(int amountToGive) {
        this.amountToGive = amountToGive;
    }

    public int getPreviousEXPTotal() {
        return this.previousEXPTotal;
    }

    public void setPreviousEXPTotal(int previousEXPTotal) {
        this.previousEXPTotal = previousEXPTotal;
    }

    public Player getPlayer() {
        return this.player;
    }
}

