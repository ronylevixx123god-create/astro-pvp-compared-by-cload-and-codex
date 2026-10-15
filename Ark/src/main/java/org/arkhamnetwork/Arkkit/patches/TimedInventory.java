/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.inventory.Inventory;

class TimedInventory {
    private final long expiration;
    private final Inventory inventory;

    public TimedInventory(long expiration, Inventory inventory) {
        this.expiration = expiration;
        this.inventory = inventory;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}

