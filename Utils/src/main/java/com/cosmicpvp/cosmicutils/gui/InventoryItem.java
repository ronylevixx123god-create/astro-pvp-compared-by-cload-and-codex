/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.gui;

import com.cosmicpvp.cosmicutils.itembuilder.CosmicItemBuilder;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryItem {
    private ItemStack item;
    private Map<ClickType, Runnable> clickMap = new HashMap<ClickType, Runnable>();
    private Runnable runnable;

    public InventoryItem(ItemStack original) {
        this.item = original;
    }

    public InventoryItem(CosmicItemBuilder original) {
        this(original.build());
    }

    public InventoryItem click(ClickType type, Runnable runnable) {
        this.clickMap.put(type, runnable);
        return this;
    }

    public InventoryItem click(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public void handleClick(InventoryClickEvent event) {
        if (this.clickMap.isEmpty() && this.runnable != null) {
            this.runnable.run();
        } else {
            Runnable found = this.clickMap.get((Object) event.getClick());
            if (found != null) {
                found.run();
            }
        }
    }

    public ItemStack getItem() {
        return this.item;
    }

    public Map<ClickType, Runnable> getClickMap() {
        return this.clickMap;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }
}

