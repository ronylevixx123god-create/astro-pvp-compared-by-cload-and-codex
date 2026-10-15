/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.Material
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.enchantments.enchantments.tools;

import com.rit.sucy.EnchantmentAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

final class DetonateListener
implements Listener {
    DetonateListener() {
    }

    @EventHandler(ignoreCancelled=true)
    public void onTagBlockFace(PlayerInteractEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        if (event.getBlockFace() == null) {
            return;
        }
        if (event.getItem() == null || event.getMaterial() == Material.AIR) {
            return;
        }
        if (!EnchantmentAPI.itemHasEnchantment((ItemStack)event.getItem(), (String)"Detonate")) {
            return;
        }
        Detonate.updateSavedBlockFace(event.getPlayer(), event.getBlockFace());
    }
}

