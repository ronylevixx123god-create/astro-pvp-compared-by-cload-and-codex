/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AlchemistTradeEvent
        extends Event {
    public static HandlerList list = new HandlerList();
    public Player player;
    public List<ItemStack> tradedItems;
    public List<ItemStack> willReceive;

    public AlchemistTradeEvent(Player player, List<ItemStack> tradedItems, List<ItemStack> willReceive) {
        this.player = player;
        this.tradedItems = tradedItems;
        this.willReceive = willReceive;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
}

