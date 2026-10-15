/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

final class ItemNametagListener
        implements Listener {
    public static List<Material> canRename;

    ItemNametagListener() {
        canRename = new ArrayList<Material>();
        canRename.add(Material.WOOD_SWORD);
        canRename.add(Material.STONE_SWORD);
        canRename.add(Material.GOLD_SWORD);
        canRename.add(Material.IRON_SWORD);
        canRename.add(Material.DIAMOND_SWORD);
        canRename.add(Material.WOOD_AXE);
        canRename.add(Material.STONE_AXE);
        canRename.add(Material.GOLD_AXE);
        canRename.add(Material.IRON_AXE);
        canRename.add(Material.DIAMOND_AXE);
        canRename.add(Material.WOOD_SPADE);
        canRename.add(Material.STONE_SPADE);
        canRename.add(Material.GOLD_SPADE);
        canRename.add(Material.IRON_SPADE);
        canRename.add(Material.DIAMOND_SPADE);
        canRename.add(Material.WOOD_PICKAXE);
        canRename.add(Material.STONE_PICKAXE);
        canRename.add(Material.GOLD_PICKAXE);
        canRename.add(Material.IRON_PICKAXE);
        canRename.add(Material.DIAMOND_PICKAXE);
        canRename.add(Material.WOOD_HOE);
        canRename.add(Material.STONE_HOE);
        canRename.add(Material.GOLD_HOE);
        canRename.add(Material.IRON_HOE);
        canRename.add(Material.DIAMOND_HOE);
        canRename.add(Material.BOW);
        canRename.add(Material.LEATHER_HELMET);
        canRename.add(Material.LEATHER_CHESTPLATE);
        canRename.add(Material.LEATHER_LEGGINGS);
        canRename.add(Material.LEATHER_BOOTS);
        canRename.add(Material.CHAINMAIL_HELMET);
        canRename.add(Material.CHAINMAIL_CHESTPLATE);
        canRename.add(Material.CHAINMAIL_LEGGINGS);
        canRename.add(Material.CHAINMAIL_BOOTS);
        canRename.add(Material.GOLD_HELMET);
        canRename.add(Material.GOLD_CHESTPLATE);
        canRename.add(Material.GOLD_LEGGINGS);
        canRename.add(Material.GOLD_BOOTS);
        canRename.add(Material.IRON_HELMET);
        canRename.add(Material.IRON_CHESTPLATE);
        canRename.add(Material.IRON_LEGGINGS);
        canRename.add(Material.IRON_BOOTS);
        canRename.add(Material.DIAMOND_HELMET);
        canRename.add(Material.DIAMOND_CHESTPLATE);
        canRename.add(Material.DIAMOND_LEGGINGS);
        canRename.add(Material.DIAMOND_BOOTS);
        canRename.add(Material.FISHING_ROD);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && ItemNametag.isNametagItem(e.getItem())) {
            Player pl = e.getPlayer();
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            pl.updateInventory();
            if (pl.hasMetadata(ItemNametag.pending_itemtag_metadata)) {
                pl.sendMessage(ChatColor.RED + "You already have a pending Item Nametag queued.");
                pl.sendMessage(ChatColor.GRAY + "Type '" + ChatColor.RED + "cancel" + ChatColor.GRAY + "' to unqueue this opperation.");
                return;
            }
            int amt = e.getItem().getAmount();
            if (amt == 1) {
                pl.setItemInHand(null);
            } else if (amt > 1) {
                ItemStack i = e.getItem();
                i.setAmount(i.getAmount() - 1);
                pl.setItemInHand(i);
            }
            pl.setMetadata(ItemNametag.pending_itemtag_metadata, new FixedMetadataValue(Arkkit.get(), true));
            pl.sendMessage("");
            pl.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "     Itemtag Usage");
            pl.sendMessage(ChatColor.GOLD + "1. " + ChatColor.GRAY + "Hold the ITEM you'd like to rename.");
            pl.sendMessage(ChatColor.GOLD + "2. " + ChatColor.GRAY + "Send the new name as a chat message with & color codes.");
            pl.sendMessage(ChatColor.GOLD + "3. " + ChatColor.GRAY + "Confirm the preview of the new name that is displayed.");
            pl.sendMessage("");
            pl.playSound(pl.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.5f);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasMetadata(ItemNametag.pending_itemtag_metadata)) {
            Player pl = e.getPlayer();
            e.setCancelled(true);
            if (e.getPlayer().hasMetadata(ItemNametag.itemtag_confirm_metadata) && e.getMessage().equalsIgnoreCase("confirm")) {
                String custom_name = pl.getMetadata(ItemNametag.itemtag_confirm_metadata).get(0).asString();
                ItemStack is = pl.getItemInHand();
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(custom_name);
                is.setItemMeta(im);
                pl.setItemInHand(is);
                pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Your ITEM has been renamed to: \"" + custom_name + ChatColor.YELLOW + "\"");
                pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
                pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.5f);
                e.setMessage("");
                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(pl, "ITEM_USE", "NAMETAG"));
                return;
            }
            if (e.getMessage().equalsIgnoreCase("cancel") || e.getPlayer().hasMetadata(ItemNametag.itemtag_confirm_metadata) && !e.getMessage().equalsIgnoreCase("confirm")) {
                pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
                pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
                pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "** " + ChatColor.RED + "You have unqueued your Itemtag for usage.");
                if (pl.getInventory().firstEmpty() != -1) {
                    Bukkit.getScheduler().runTask(Arkkit.get(), () -> pl.getInventory().addItem(new ItemStack[]{ItemNametag.getNametagItem()}));
                } else {
                    Bukkit.getScheduler().runTask(Arkkit.get(), () -> {
                        pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, the nametag has been dropped at your feet.");
                        pl.getWorld().dropItem(pl.getLocation(), ItemNametag.getNametagItem());
                    });
                }
            } else if (pl.getItemInHand() != null) {
                ItemStack is = pl.getItemInHand();
                if (is == null || is.getType() == Material.AIR) {
                    pl.sendMessage(ChatColor.RED + "Hold the ITEM you'd like to rename.");
                    pl.sendMessage(ChatColor.GRAY + "Type '" + ChatColor.RED + "cancel" + ChatColor.GRAY + "' to unqueue this opperation.");
                    return;
                }
                if (!canRename.contains(is.getType())) {
                    pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "INVALID ITEM -- WEAPONS AND ARMOR ONLY!");
                    pl.sendMessage(ChatColor.RED + "Hold the ITEM you'd like to rename.");
                    pl.sendMessage(ChatColor.GRAY + "Type '" + ChatColor.RED + "cancel" + ChatColor.GRAY + "' to unqueue this opperation.");
                    return;
                }
                ItemMeta im = is.getItemMeta();
                if (e.getMessage().length() <= 0) {
                    pl.sendMessage(ChatColor.RED + "Your custom name must contain atleast 1 character.");
                    return;
                }
                if (e.getMessage().length() > 64) {
                    pl.sendMessage(ChatColor.RED + "Your custom name cannot exceed 64 characters.");
                    return;
                }
                if (e.getMessage().toLowerCase().contains("&k") || e.getMessage().toLowerCase().contains("\u00a7k")) {
                    pl.sendMessage(ChatColor.RED + "You cannot use the &k (magic) color code.");
                    return;
                }
                if (e.getMessage().contains("[") || e.getMessage().contains("]")) {
                    pl.sendMessage(ChatColor.RED + "You cannot use [ or ] brackets in item names.");
                    return;
                }
                if (ChatUtils.isSwearMessage(e.getMessage())) {
                    pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "Your custom name contains illegal phrases.");
                    pl.sendMessage(ChatColor.GRAY + "Problematic phrases: " + ChatUtils.getFilteredSwearMessage(e.getMessage(), false));
                    pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "WARNING: " + ChatColor.GRAY + "Attempting to bypass this filter will result in your item disappearing.");
                    return;
                }
                String custom_name = ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + e.getMessage());
                if (custom_name.contains(ChatColor.MAGIC.toString())) {
                    pl.sendMessage(ChatColor.RED + "You cannot use the &k (magic) color code.");
                    return;
                }
                pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "(!) " + ChatColor.YELLOW + "Item Name Preview: " + custom_name);
                pl.sendMessage(ChatColor.GRAY + "Type '" + ChatColor.GREEN + "confirm" + ChatColor.GRAY + "' if this looks correct, otherwise type '" + ChatColor.RED + "cancel" + ChatColor.GRAY + "' to start over.");
                pl.setMetadata(ItemNametag.itemtag_confirm_metadata, new FixedMetadataValue(Arkkit.get(), custom_name));
            } else {
                pl.sendMessage(ChatColor.RED + "Hold the ITEM you'd like to rename.");
                pl.sendMessage(ChatColor.GRAY + "Type '" + ChatColor.RED + "cancel" + ChatColor.GRAY + "' to unqueue this opperation.");
            }
            e.setMessage("");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player pl = e.getPlayer();
        if (pl.hasMetadata(ItemNametag.pending_itemtag_metadata)) {
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(ItemNametag.getNametagItem());
            }
            pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
            pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player pl = (Player) e.getWhoClicked();
        if (pl.hasMetadata(ItemNametag.itemtag_confirm_metadata)) {
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "** " + ChatColor.RED + "Item rename cancelled because you clicked an item.");
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(ItemNametag.getNametagItem());
            } else {
                pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, the nametag has been dropped at your feet.");
                pl.getWorld().dropItem(pl.getLocation(), ItemNametag.getNametagItem());
            }
            pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
            pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        Player pl = e.getPlayer();
        if (pl.hasMetadata(ItemNametag.itemtag_confirm_metadata)) {
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "** " + ChatColor.RED + "Item rename cancelled because you picked up an item.");
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(ItemNametag.getNametagItem());
            } else {
                pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, the nametag has been dropped at your feet.");
                pl.getWorld().dropItem(pl.getLocation(), ItemNametag.getNametagItem());
            }
            pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
            pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player pl = e.getPlayer();
        if (pl.hasMetadata(ItemNametag.itemtag_confirm_metadata)) {
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "** " + ChatColor.RED + "Item rename cancelled because you dropped an item.");
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(ItemNametag.getNametagItem());
            } else {
                pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, the nametag has been dropped at your feet.");
                pl.getWorld().dropItem(pl.getLocation(), ItemNametag.getNametagItem());
            }
            pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
            pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
        Player pl = e.getPlayer();
        if (pl.hasMetadata(ItemNametag.itemtag_confirm_metadata)) {
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "** " + ChatColor.RED + "Item rename cancelled because you changed your held item.");
            if (pl.getInventory().firstEmpty() != -1) {
                pl.getInventory().addItem(ItemNametag.getNametagItem());
            } else {
                pl.sendMessage(ChatColor.RED + "(!) You had no room in your inventory, the nametag has been dropped at your feet.");
                pl.getWorld().dropItem(pl.getLocation(), ItemNametag.getNametagItem());
            }
            pl.removeMetadata(ItemNametag.itemtag_confirm_metadata, Arkkit.get());
            pl.removeMetadata(ItemNametag.pending_itemtag_metadata, Arkkit.get());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (e.getPlayer().getItemInHand() != null && ItemNametag.isNametagItem(e.getPlayer().getItemInHand())) {
            e.setCancelled(true);
        }
    }
}

