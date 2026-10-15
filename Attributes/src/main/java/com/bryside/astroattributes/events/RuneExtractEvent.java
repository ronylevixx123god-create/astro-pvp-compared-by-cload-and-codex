package com.bryside.astroattributes.events;

import com.bryside.astroattributes.enums.RuneType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public final class RuneExtractEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player player;
    private RuneType type;
    private int level;
    private int success;
    private int experience;
    private ItemStack removed;

    public RuneExtractEvent(Player player, ItemStack removed, RuneType type, int success, int level, int experience) {
        this.player = player;
        this.success = success;
        this.level = level;
        this.type = type;
        this.removed = removed;
        this.experience = experience;
    }

    public static final HandlerList getHandlerList() {
        return handlers;
    }

    public final HandlerList getHandlers() {
        return handlers;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public RuneType getRuneType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSuccess() {
        return this.success;
    }

    public ItemStack getItem() {
        return this.removed;
    }

    public void increaseSuccessRate(int success) {
        this.success += success;
        if (this.success > 100) {
            this.success = 100;
        }

    }

    public void setSuccessRate(int success) {
        this.success = success;
    }

    public void increaseLevel(int level) {
        this.level += level;
    }

    public void increaseExperience(int experience) {
        this.experience += experience;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
