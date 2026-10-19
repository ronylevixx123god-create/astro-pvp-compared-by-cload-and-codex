//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public interface Menu extends InventoryHolder
{
    default void onClick(final Player player, final InventoryClickEvent event) {
    }

    default void onOpen(final Player player) {
    }

    default void onClose(final Player player) {
    }
}
