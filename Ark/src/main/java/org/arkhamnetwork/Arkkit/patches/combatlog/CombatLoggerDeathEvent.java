/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches.combatlog;

import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CombatLoggerDeathEvent
        extends Event
        implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity combatLoggerEntity;
    private final String playerName;
    private final String playerUUIDString;
    private final EntityDamageEvent rawEvent;
    private boolean cancelled;
    private List<ItemStack> inventoryItems;

    public CombatLoggerDeathEvent(LivingEntity combatLoggerEntity, String playerUUIDString, String playerName, EntityDamageEvent event, ItemStack[] itemsFromMetadata) {
        this.combatLoggerEntity = combatLoggerEntity;
        this.rawEvent = event;
        this.playerName = playerName;
        this.playerUUIDString = playerUUIDString;
        if (itemsFromMetadata != null) {
            this.inventoryItems = Lists.newArrayList(itemsFromMetadata);
        }
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public LivingEntity getCombatLoggerEntity() {
        return this.combatLoggerEntity;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getPlayerUUIDString() {
        return this.playerUUIDString;
    }

    public List<ItemStack> getInventoryItems() {
        return this.inventoryItems;
    }

    public EntityDamageEvent getRawEvent() {
        return this.rawEvent;
    }
}

