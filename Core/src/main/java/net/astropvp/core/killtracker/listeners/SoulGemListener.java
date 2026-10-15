/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 */
package net.astropvp.core.killtracker.listeners;

import net.astropvp.core.killtracker.KillTrackerAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SoulGemListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onInventoryclick(InventoryClickEvent e) {
        if (e.getCursor() != null && e.getCurrentItem() != null && KillTrackerAPI.isSoulGem(e.getCursor()) && KillTrackerAPI.isSoulGem(e.getCurrentItem()) && e.getCursor().getType() == Material.EMERALD) {
            Player p = (Player)e.getWhoClicked();
            if (e.getCurrentItem().getAmount() > 1) {
                e.setCancelled(true);
                p.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You cannot combine stacked Soul Gems.");
                return;
            }
            int cursorSouls = KillTrackerAPI.getSouls(e.getCursor());
            int currentSouls = KillTrackerAPI.getSouls(e.getCurrentItem());
            if (KillTrackerAPI.isSameID(e.getCurrentItem(), e.getCursor())) {
                p.setItemOnCursor(null);
                e.setCancelled(true);
                e.setCursor(null);
                Bukkit.getLogger().info("Removing duplicate Soul Gems (" + cursorSouls + ")  (" + currentSouls + ") from " + p.getName() + "!");
                return;
            }
            if (cursorSouls == -1 || currentSouls == -1) {
                return;
            }
            int newSouls = currentSouls + cursorSouls;
            e.setCancelled(true);
            if (e.getCursor().getAmount() > 1) {
                e.getCursor().setAmount(e.getCursor().getAmount() - 1);
            } else {
                p.setItemOnCursor(null);
            }
            KillTrackerAPI.setSouls(e.getCurrentItem(), newSouls);
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.0f, 1.4f);
            p.updateInventory();
        }
    }
}

