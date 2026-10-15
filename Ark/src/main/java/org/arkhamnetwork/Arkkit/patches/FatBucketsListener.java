/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerBucketFillEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

final class FatBucketsListener
        implements Listener {
    FatBucketsListener() {
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerEmptyFatBucket(PlayerBucketEmptyEvent e) {
        ItemStack bucket = e.getPlayer().getItemInHand();
        if (FatBuckets.isFatBucket(bucket)) {
            int uses = FatBuckets.getFatBucketUses(bucket) - 1;
            bucket = FatBuckets.setFatBucketUses(bucket, uses);
            e.setItemStack(bucket);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerFillFatBucket(PlayerBucketFillEvent e) {
        ItemStack bucket = e.getPlayer().getItemInHand();
        if (FatBuckets.isFatBucket(bucket)) {
            boolean inNether;
            boolean isLava = e.getBlockClicked().getType() == Material.LAVA || e.getBlockClicked().getType() == Material.STATIONARY_LAVA;
            boolean bl = inNether = e.getBlockClicked().getWorld().getEnvironment() == World.Environment.NETHER;
            if (!isLava || !inNether) {
                if (!isLava) {
                    e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "Fat Buckets can only be filled with lava!");
                } else if (!inNether) {
                    e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "Fat Buckets can only be filled in the Nether!");
                }
                e.setCancelled(true);
                return;
            }
            int charges = FatBuckets.getFatBucketCharges(bucket) + 1;
            bucket = charges >= FatBuckets.maxBucketCharges ? FatBuckets.setFatBucketUses(FatBuckets.setFatBucketCharges(bucket, 0), 256) : FatBuckets.setFatBucketCharges(bucket, charges);
            e.setItemStack(bucket);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryType inventoryType = e.getInventory().getType();
        if ((inventoryType == InventoryType.FURNACE || inventoryType == InventoryType.DISPENSER || inventoryType == InventoryType.DROPPER) && (FatBuckets.isFatBucket(e.getCurrentItem()) || FatBuckets.isFatBucket(e.getCursor()))) {
            Player p = (Player) e.getWhoClicked();
            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "Fat Buckets cannot be used in furnaces, dispensers, or droppers!");
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }
}

