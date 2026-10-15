/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 */
package com.cosmicpvp.cosmicutils.inventory;


import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Optional;

public abstract class DecimateInventory
        implements InventoryProvider {
    private final String title;
    private final int rows;
    private final int columns;
    protected Optional<InventoryListener> listener = Optional.empty();
    protected boolean clickable = false;

    public DecimateInventory(String title, int size) {
        this.title = title;
        this.rows = size / 9;
        this.columns = 9;
    }

    public DecimateInventory(String title, int rows, int columns) {
        this.title = title;
        this.rows = rows;
        this.columns = columns;
    }

    public DecimateInventory(String title, int rows, int columns, InventoryListener listener) {
        this.title = title;
        this.rows = rows;
        this.columns = columns;
        this.listener = Optional.of(listener);
    }

    public DecimateInventory(String title, int size, InventoryListener listener) {
        this.title = title;
        this.rows = size / 9;
        this.columns = 9;
        this.listener = Optional.of(listener);
    }

    public DecimateInventory(String title, int rows, int columns, InventoryListener listener, boolean clickable) {
        this.title = title;
        this.rows = rows;
        this.columns = columns;
        this.listener = Optional.of(listener);
        this.clickable = clickable;
    }

    public SmartInventory build() {
        if (this.listener.isPresent()) {
            return SmartInventory.builder().type(InventoryType.CHEST).size(this.rows, this.columns).provider(this).manager(CosmicUtils.get().getInventoryManager()).listener(this.listener.get()).clickable(this.clickable).title(this.title).build();
        }
        return SmartInventory.builder().type(InventoryType.CHEST).size(this.rows, this.columns).provider(this).manager(CosmicUtils.get().getInventoryManager()).clickable(this.clickable).title(this.title).build();
    }

    public void open(Player player) {
        this.build().open(player);
    }

    @Override
    public abstract void init(Player var1, InventoryContents var2);

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}

