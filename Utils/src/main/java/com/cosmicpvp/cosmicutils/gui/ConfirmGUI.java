/*
 * Decompiled with CFR 0.150.
 */
package com.cosmicpvp.cosmicutils.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfirmGUI
        extends CosmicGUI {
    private InventoryItem confirmItem;
    private InventoryItem cancelItem;
    private InventoryItem fillerItem;
    private boolean cancelled = false;
    private boolean closed = false;
    private boolean confirmed = false;

    public ConfirmGUI(Player player, String title, int size, ItemStack confirmButton, ItemStack cancelButton, ItemStack fillerPane, Runnable confirmConsumer, Runnable cancelConsumer) {
        super(player, title, size);
        this.fillerItem = new InventoryItem(fillerPane);
        this.cancelItem = new InventoryItem(cancelButton).click(() -> {
            this.cancelled = true;
            cancelConsumer.run();
        });
        this.confirmItem = new InventoryItem(confirmButton).click(() -> {
            this.confirmed = true;
            confirmConsumer.run();
        });
    }

    @Override
    public void onInventoryClose() {
        if (this.cancelled || this.closed || this.confirmed) {
            return;
        }
        this.closed = true;
        if (this.cancelItem.getRunnable() != null) {
            this.cancelItem.getRunnable().run();
        }
        super.onInventoryClose();
    }

    @Override
    public void redraw() {
        int i;
        this.cancelled = false;
        for (i = 0; i < 4; ++i) {
            this.setItem(i, this.confirmItem);
        }
        for (i = 5; i < this.size; ++i) {
            this.setItem(i, this.cancelItem);
        }
        for (i = 0; i < this.size; ++i) {
            ItemStack current = this.inventory.getItem(i);
            if (current != null && current.getType() != Material.AIR) continue;
            this.setItem(i, this.fillerItem);
        }
    }

    public InventoryItem getConfirmItem() {
        return this.confirmItem;
    }

    public InventoryItem getCancelItem() {
        return this.cancelItem;
    }

    public InventoryItem getFillerItem() {
        return this.fillerItem;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }
}

