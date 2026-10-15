/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.CraftItemEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.PrepareItemCraftEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.event.world.ChunkUnloadEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

final class AntiMinecartListener
        implements Listener {
    AntiMinecartListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoad(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity.getType() != EntityType.MINECART && entity.getType() != EntityType.MINECART_CHEST && entity.getType() != EntityType.MINECART_COMMAND && entity.getType() != EntityType.MINECART_FURNACE && entity.getType() != EntityType.MINECART_HOPPER && entity.getType() != EntityType.MINECART_MOB_SPAWNER && entity.getType() != EntityType.MINECART_TNT)
                continue;
            entity.remove();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUnload(ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity.getType() != EntityType.MINECART && entity.getType() != EntityType.MINECART_CHEST && entity.getType() != EntityType.MINECART_COMMAND && entity.getType() != EntityType.MINECART_FURNACE && entity.getType() != EntityType.MINECART_HOPPER && entity.getType() != EntityType.MINECART_MOB_SPAWNER && entity.getType() != EntityType.MINECART_TNT)
                continue;
            entity.remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.MINECART || block.getType() == Material.POWERED_MINECART || block.getType() == Material.POWERED_RAIL || block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + "" + ChatColor.RED + " place minecarts or rails.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getItem().getType() == null) {
            return;
        }
        ItemStack block = event.getItem();
        if (block.getType() == Material.MINECART || block.getType() == Material.POWERED_MINECART || block.getType() == Material.POWERED_RAIL || block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + "" + ChatColor.RED + " place minecarts or rails.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack block = event.getItemDrop().getItemStack();
        if (block != null && (block.getType() == Material.MINECART || block.getType() == Material.POWERED_MINECART || block.getType() == Material.POWERED_RAIL || block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
            event.getItemDrop().remove();
            event.getPlayer().sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + "" + ChatColor.RED + " drop minecarts or rails.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent event) {
        ItemStack block = event.getItem().getItemStack();
        if (block != null && (block.getType() == Material.MINECART || block.getType() == Material.POWERED_MINECART || block.getType() == Material.POWERED_RAIL || block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
            event.getItem().remove();
            event.getPlayer().sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + "" + ChatColor.RED + " pickup minecarts or rails.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        ItemStack block = event.getRecipe().getResult();
        if (block != null && (block.getType() == Material.MINECART || block.getType() == Material.POWERED_MINECART || block.getType() == Material.POWERED_RAIL || block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL)) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(CraftItemEvent event) {
        ItemStack block = event.getRecipe().getResult();
        if (block != null && (block.getType() == Material.MINECART || block.getType() == Material.POWERED_MINECART || block.getType() == Material.POWERED_RAIL || block.getType() == Material.RAILS || block.getType() == Material.DETECTOR_RAIL)) {
            event.getInventory().setResult(null);
            event.setCancelled(true);
            event.setCurrentItem(null);
            ((Player) event.getWhoClicked()).updateInventory();
            event.getWhoClicked().sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + "" + ChatColor.RED + " craft minecarts or rails.");
        }
    }
}

