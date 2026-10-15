package org.arkhamnetwork.enchantments.events;

import com.rit.sucy.CustomEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomEnchantProcOnEntityEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private LivingEntity player;
    private LivingEntity target;
    private Event event;
    private CustomEnchantment enchant;
    private int enchantLevel;

    public CustomEnchantProcOnEntityEvent(LivingEntity player, LivingEntity target, Event rawEvent, CustomEnchantment enchant, int enchantLevel) {
        this.player = player;
        this.enchant = enchant;
        this.target = target;
        this.event = rawEvent;
        this.enchantLevel = enchantLevel;
    }

    public CustomEnchantProcOnEntityEvent(boolean cancelled, LivingEntity player, LivingEntity target, Event event, CustomEnchantment enchant, int enchantLevel) {
        this.cancelled = cancelled;
        this.player = player;
        this.target = target;
        this.event = event;
        this.enchant = enchant;
        this.enchantLevel = enchantLevel;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public LivingEntity getPlayer() {
        return this.player;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public CustomEnchantment getEnchant() {
        return this.enchant;
    }

    public int getEnchantLevel() {
        return this.enchantLevel;
    }
}
