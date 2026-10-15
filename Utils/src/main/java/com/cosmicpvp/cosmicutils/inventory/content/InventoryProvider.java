/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.cosmicpvp.cosmicutils.inventory.content;

import org.bukkit.entity.Player;

public interface InventoryProvider {
    void init(Player var1, InventoryContents var2);

    default void update(Player player, InventoryContents contents) {
    }
}

