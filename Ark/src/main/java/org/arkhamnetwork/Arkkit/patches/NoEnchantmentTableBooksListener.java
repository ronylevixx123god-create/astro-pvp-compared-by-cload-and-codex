/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.enchantment.EnchantItemEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

final class NoEnchantmentTableBooksListener
        implements Listener {
    NoEnchantmentTableBooksListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals("Enchant") && e.getCurrentItem() != null && (e.getCurrentItem().getType() == Material.BOOK || e.getCurrentItem().getType() == Material.ENCHANTED_BOOK)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            e.getWhoClicked().sendMessage(ChatColor.RED + "You cannot currently inscribe enchantments onto books.");
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent e) {
        if (e.getItem().getType() == Material.BOOK || e.getItem().getType() == Material.ENCHANTED_BOOK) {
            e.getEnchanter().sendMessage(ChatColor.RED + "You cannot currently inscribe enchantments onto books.");
            e.getEnchanter().closeInventory();
            e.setCancelled(true);
        }
    }
}

