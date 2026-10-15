package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

final class ItemIDListener
        implements Listener {
    ItemIDListener() {
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        ItemStack i;
        if (e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase("container.crafting") && (i = e.getCurrentItem()) != null && (i.hasItemMeta() && i.getItemMeta().hasLore() || i.getType() == Material.MOB_SPAWNER) && (i.getMaxStackSize() == 1 && (GeneralUtil.isArmor(i) || GeneralUtil.isWeapon(i) || GeneralUtil.isTool(i)) || ItemID.otherItemsToMonitor.contains(i.getType()) && ItemID.hasValidDurabilityForTracking(i))) {
            long iid = ItemID.getItemID(e.getCurrentItem());
            if (iid == -1L) {
                if (i.getAmount() > 1 && ItemID.otherItemsToMonitor.contains(i.getType()) && ItemID.hasValidDurabilityForTracking(i)) {
                    return;
                }
                ItemID.assignItemID(e, e.getCurrentItem());
            } else if (ItemID.blacklist_iid.contains(iid)) {
                Bukkit.getLogger().info("[ItemID] Blacklisted IID #" + iid + " found on player " + e.getWhoClicked().getName() + " at " + e.getWhoClicked().getLocation() + ", item: " + e.getCurrentItem());
                e.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "An item in your inventory, with IID " + iid + ", has been flagged as a duplicated item, and has been removed.");
                e.setCurrentItem(null);
            } else if (iid > ItemID.next_iid) {
                Bukkit.getLogger().info("[ItemID] Found item with IID " + iid + ", which is > next_iid value of " + ItemID.next_iid + ", rerolling iid.");
                ItemID.next_iid += 100000L;
                ItemID.setItemID(e.getCurrentItem(), ItemID.generateNewItemID());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerUseBlacklistedItem(PlayerInteractEvent e) {
        long iid;
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && ItemID.otherItemsToMonitor.contains(e.getItem().getType()) && ItemID.hasValidDurabilityForTracking(e.getItem()) && (iid = ItemID.getItemID(e.getItem())) != -1L && ItemID.blacklist_iid.contains(iid)) {
            Bukkit.getLogger().info("[ItemID] Blacklisted IID #" + iid + " used by player " + e.getPlayer().getName() + " at " + e.getPlayer().getLocation() + ", item: " + e.getItem());
            e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "An item you just attempted to use, with IID " + iid + ", has been flagged as a duplicated item, and has been removed.");
            e.getPlayer().setItemInHand(null);
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.getItem().setAmount(0);
            e.getItem().setType(Material.AIR);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        long iid;
        if (e.getItemInHand().getType() == Material.MOB_SPAWNER && (iid = ItemID.getItemID(e.getItemInHand())) != -1L) {
            if (ItemID.blacklist_iid.contains(iid)) {
                Bukkit.getLogger().info("[ItemID] Blacklisted IID #" + iid + " placed by player " + e.getPlayer().getName() + " at " + e.getPlayer().getLocation() + ", item: " + e.getItemInHand());
                e.setCancelled(true);
                e.getPlayer().setItemInHand(null);
                e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "An item you just attempted to place, with IID " + iid + ", has been flagged as a duplicated item, and has been removed.");
                return;
            }
            ItemID.blacklistIID(iid, "blockPlaceEvent");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemMove(InventoryPickupItemEvent e) {
        ItemStack i;
        if (e.getItem().getItemStack().getType() == Material.MOB_SPAWNER && ItemID.needsItemID(i = e.getItem().getItemStack())) {
            if (i.getAmount() == 1) {
                ItemID.setItemID(i, ItemID.generateNewItemID());
                e.getItem().setItemStack(i);
            } else if (i.getAmount() > 1) {
                ItemStack iC = i.clone();
                iC.setAmount(1);
                World w = e.getItem().getWorld();
                Location l = e.getItem().getLocation().clone();
                e.getItem().remove();
                e.setCancelled(true);
                for (int x = i.getAmount(); x > 0; --x) {
                    if (e.getInventory().firstEmpty() == -1) {
                        ItemStack remainder = iC.clone();
                        remainder.setAmount(x);
                        w.dropItem(l, remainder);
                        break;
                    }
                    ItemStack unique = iC.clone();
                    ItemID.setItemID(unique, ItemID.generateNewItemID());
                    e.getInventory().addItem(unique);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent e) {
        ItemStack i;
        if (e.getItem().getItemStack().getType() == Material.MOB_SPAWNER && ItemID.needsItemID(i = e.getItem().getItemStack())) {
            if (i.getAmount() == 1) {
                ItemID.setItemID(i, ItemID.generateNewItemID());
                e.getItem().setItemStack(i);
            } else if (i.getAmount() > 1) {
                ItemStack iC = i.clone();
                iC.setAmount(1);
                Player p = e.getPlayer();
                e.getItem().remove();
                e.setCancelled(true);
                for (int x = i.getAmount(); x > 0; --x) {
                    if (p.getInventory().firstEmpty() == -1) {
                        ItemStack remainder = iC.clone();
                        remainder.setAmount(x);
                        p.getWorld().dropItem(p.getLocation(), remainder);
                        break;
                    }
                    ItemStack unique = iC.clone();
                    ItemID.setItemID(unique, ItemID.generateNewItemID());
                    p.getInventory().addItem(unique);
                }
            }
        }
    }
}