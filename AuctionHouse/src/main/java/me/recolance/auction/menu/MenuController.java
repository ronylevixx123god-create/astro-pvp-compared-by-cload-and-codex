/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.recolance.auction.menu;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.controller.Controller;
import me.recolance.auction.data.DataHolder;
import me.recolance.auction.menu.bidmenus.BidMenu;
import me.recolance.auction.menu.categories.CategoryMainMenu;
import me.recolance.auction.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MenuController
        implements Listener {
    private static HashMap<UUID, Inventory> closeSecurity = new HashMap();
    private static HashMap<UUID, Long> timer = new HashMap();
    private static Economy economy = Auction.economy;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        ItemStack item;
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (closeSecurity.containsKey(player.getUniqueId()) && closeSecurity.get(player.getUniqueId()) == inventory) {
            event.setCancelled(true);
            return;
        }
        if (!MenuHelper.isViewing(player)) {
            return;
        }
        if (!timer.containsKey(player.getUniqueId())) {
            timer.put(player.getUniqueId(), System.currentTimeMillis());
        } else if (System.currentTimeMillis() - timer.get(player.getUniqueId()) < 350L) {
            event.setCancelled(true);
            return;
        }
        timer.put(player.getUniqueId(), System.currentTimeMillis());
        if (event.getCurrentItem() != null && !event.getClickedInventory().equals((Object) player.getOpenInventory().getTopInventory())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), ((Player) player)::closeInventory, 1L);
            Util.message(player, "&e&l(!) &eTo list an item for sale on the &6/ah&e use &6/ah sell <price>&e, to list an item up for auction use &6/ah startbid&e.");
            return;
        }
        int slot = event.getRawSlot();
        if (MenuUtil.isCancellableClick(inventory, slot, event.getClick())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            player.updateInventory();
        }
        if ((item = event.getCurrentItem()) == null || item.getType() == Material.AIR) {
            return;
        }
        switch (MenuHelper.getViewing(player)) {
            case AUCTION: {
                if (slot < 45 && slot >= 0) {
                    List<Listing> vL = MenuHelper.getViewingListings(player);
                    if (vL == null) {
                        Bukkit.getLogger().info("[AuctionHouse] Player " + player.getName() + " has a null vL object, closing inventory.");
                        player.closeInventory();
                        return;
                    }
                    Listing l = vL.get(slot);
                    if (event.getClick() == ClickType.SHIFT_RIGHT && player.hasPermission("auction.admin.expire")) {
                        Controller.adminCancel(player, l);
                        MenuRenderer.renderAuctionPage(MenuHelper.getViewingPage(player), inventory, player);
                        break;
                    }
                    if (!DataHolder.getListings().contains(l)) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "This item is no longer available.");
                        break;
                    }
                    if (l.getWhoListed().equals(player.getUniqueId())) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "To cancel your auction, click the diamond.");
                        break;
                    }
                    if (!Util.hasInventorySpace(player)) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "You do not have enough inventory space!");
                        break;
                    }
                    if (!economy.has(Bukkit.getOfflinePlayer((UUID) player.getUniqueId()), (double) l.getPrice())) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "You cannot afford this item.");
                        break;
                    }
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater((Plugin) Auction.getPlugin(), () -> MenuRenderer.purchaseConfirmation(player, l), 1L);
                    break;
                }
                if (slot == 45) {
                    player.closeInventory();
                    MenuRenderer.selling(player);
                    break;
                }
                if (slot == 46) {
                    player.closeInventory();
                    MenuRenderer.expired(player);
                    break;
                }
                if (slot == 47) {
                    if (item.getType() == Material.ENDER_CHEST) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> {
                            player.closeInventory();
                            player.performCommand("ah collect");
                        }, 1L);
                        break;
                    }
                    if (item.getType() != Material.BOOK_AND_QUILL) break;
                    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> {
                        player.closeInventory();
                        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> new BidMenu(player).openGUI(Auction.getPlugin()), 1L);
                    }, 1L);
                    break;
                }
                if (slot == 48 && MenuHelper.getViewingListings(player).size() != 0) {
                    MenuRenderer.renderAuctionPage(MenuHelper.getViewingPage(player) - 1, inventory, player);
                    break;
                }
                if (slot == 49) {
                    int page = MenuHelper.getViewingPage(player);
                    if (page == -1) break;
                    MenuRenderer.renderAuctionPage(page, inventory, player);
                    MenuUtil.displayMenuInfo(slot, inventory, item, "All auctions have been refreshed.");
                    break;
                }
                if (slot == 50) {
                    MenuRenderer.renderAuctionPage(MenuHelper.getViewingPage(player) + 1, inventory, player);
                    break;
                }
                if (slot != 51) break;
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> {
                    player.closeInventory();
                    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> new CategoryMainMenu(player).openGUI(Auction.getPlugin()), 1L);
                }, 1L);
                break;
            }
            case PURCHASE_CONFIRMATION: {
                boolean viewingCategories = player.hasMetadata("category_view");
                if (slot < 4) {
                    Listing l = MenuHelper.getPurchasing(player);
                    if (!DataHolder.getListings().contains(l)) {
                        if (viewingCategories) {
                            player.closeInventory();
                            player.sendMessage(CC.RedB + "(!) " + CC.Red + "Item is no longer available!");
                            Bukkit.getScheduler().runTaskLater((Plugin) Auction.getPlugin(), () -> new CategoryMainMenu(player).openGUI(Auction.getPlugin()), 1L);
                            return;
                        }
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "This item is no longer available.");
                        break;
                    }
                    if (l.getWhoListed().equals(player.getUniqueId())) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "To cancel your auction, click the diamond.");
                        break;
                    }
                    if (!Util.hasInventorySpace(player)) {
                        if (viewingCategories) {
                            player.closeInventory();
                            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You dont have enough inventory space to purchase that item!");
                            Bukkit.getScheduler().runTaskLater((Plugin) Auction.getPlugin(), () -> new CategoryMainMenu(player).openGUI(Auction.getPlugin()), 1L);
                            return;
                        }
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "You do not have enough inventory space!");
                        break;
                    }
                    if (!economy.has(Bukkit.getOfflinePlayer((UUID) player.getUniqueId()), (double) l.getPrice())) {
                        if (viewingCategories) {
                            player.closeInventory();
                            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot afford this item.");
                            Bukkit.getScheduler().runTaskLater((Plugin) Auction.getPlugin(), () -> new CategoryMainMenu(player).openGUI(Auction.getPlugin()), 1L);
                            return;
                        }
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "You cannot afford this item.");
                        break;
                    }
                    Controller.purchase(player, l);
                    player.closeInventory();
                    MenuRenderer.openAuction(player);
                    break;
                }
                if (slot <= 4 || slot >= 9) break;
                player.closeInventory();
                if (viewingCategories) {
                    Bukkit.getScheduler().runTaskLater((Plugin) Auction.getPlugin(), () -> new CategoryMainMenu(player).openGUI(Auction.getPlugin()), 1L);
                    return;
                }
                MenuRenderer.openAuction(player);
                break;
            }
            case SELLING: {
                if (slot < 45) {
                    List<Listing> listings = MenuHelper.getViewingListings(player);
                    Listing listing = listings.get(slot);
                    if (!DataHolder.getListings().contains(listing)) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "This item is no longer available.");
                        break;
                    }
                    Controller.cancel(player, listing);
                    MenuRenderer.renderSelling(player, inventory);
                    break;
                }
                if (slot != 45) break;
                player.closeInventory();
                MenuRenderer.openAuction(player);
                break;
            }
            case EXPIRED: {
                if (slot < 45) {
                    List<UnclaimedListing> listings = MenuHelper.getUnclaimedViewing(player);
                    UnclaimedListing listing = listings.get(slot);
                    if (!DataHolder.getUnclaimedListings().containsKey(player.getUniqueId()) || !DataHolder.getUnclaimedListings().get(player.getUniqueId()).contains(listing)) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "This Item No Longer Exists");
                        break;
                    }
                    if (!Util.hasInventorySpace(player)) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "You do not have enough inventory space!");
                        break;
                    }
                    Controller.claim(player, listing);
                    MenuRenderer.renderExpired(1, inventory, player);
                    break;
                }
                if (slot == 45) {
                    player.closeInventory();
                    MenuRenderer.openAuction(player);
                    break;
                }
                if (slot == 48) {
                    MenuRenderer.renderExpired(MenuHelper.getViewingPage(player) - 1, inventory, player);
                    break;
                }
                if (slot == 49) {
                    if (!DataHolder.getUnclaimedListings().containsKey(player.getUniqueId())) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "Your Collection Bin is empty!");
                        break;
                    }
                    if (!Util.hasInventorySpace(player)) {
                        MenuUtil.displayMenuInvalidity(slot, inventory, item, "You do not have enough inventory space!");
                        break;
                    }
                    Controller.claimAll(player);
                    MenuRenderer.renderExpired(1, inventory, player);
                    break;
                }
                if (slot != 50) break;
                MenuRenderer.renderExpired(MenuHelper.getViewingPage(player) + 1, inventory, player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        if (!MenuHelper.isViewing(player)) {
            return;
        }
        switch (MenuHelper.getViewing(player)) {
            case AUCTION: {
                MenuHelper.removeViewingListings(player);
                MenuHelper.removeViewingPage(player);
                break;
            }
            case PURCHASE_CONFIRMATION: {
                MenuHelper.removePurchasing(player);
                break;
            }
            case SELLING: {
                MenuHelper.removeViewingListings(player);
                break;
            }
            case EXPIRED: {
                MenuHelper.removeViewingPage(player);
                MenuHelper.removeViewingUnclaimed(player);
            }
        }
        MenuHelper.removeViewing(player);
        closeSecurity.put(player.getUniqueId(), event.getInventory());
        new BukkitRunnable() {

            public void run() {
                closeSecurity.remove(player.getUniqueId());
            }
        }.runTaskLater((Plugin) Auction.plugin, 2L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!MenuHelper.isViewing(player)) {
            return;
        }
        Inventory inventory = event.getInventory();
        Iterator iterator = event.getRawSlots().iterator();
        while (iterator.hasNext()) {
            int slot = (Integer) iterator.next();
            if (slot >= inventory.getSize()) continue;
            event.setCancelled(true);
            break;
        }
    }
}

