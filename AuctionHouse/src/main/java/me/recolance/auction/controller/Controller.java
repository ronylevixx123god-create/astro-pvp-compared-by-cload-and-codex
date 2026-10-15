/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.DecimalFormatType
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.HCFOutpostUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  me.vaqxine.NetworkManager.NetworkManager
 *  net.milkbowl.vault.economy.Economy
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.arkhamnetwork.Arkkit.net.astropvp.armorsets.commands.CommandArkReboot
 *  org.arkhamnetwork.Arkkit.patches.AntiCrash
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.recolance.auction.controller;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.struct.DecimalFormatType;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.HCFOutpostUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.command.Command;
import me.recolance.auction.data.DataHandler;
import me.recolance.auction.data.DataHolder;
import me.recolance.auction.util.AuctionUtil;
import me.recolance.auction.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.commands.CommandArkReboot;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Controller {
    private static Economy economy = Auction.economy;

    public static void purchase(Player player, Listing listing) {
        double price = listing.getPrice();
        ItemStack item = listing.getItem();
        economy.withdrawPlayer(Bukkit.getOfflinePlayer((UUID) player.getUniqueId()), (double) price);
        economy.depositPlayer(Bukkit.getOfflinePlayer((UUID) listing.getWhoListed()), (double) price);
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            long itemID = ItemID.getItemID((ItemStack) item);
            if (itemID != -1L) {
                if (ItemID.blacklist_iid.contains(itemID)) {
                    Bukkit.getLogger().info("[ItemID] Blacklisted IID #" + itemID + " was purchased from /ah by player " + player.getName() + " item: " + (Object) item);
                    player.sendMessage((Object) ChatColor.RED + "" + (Object) ChatColor.BOLD + "(!) " + (Object) ChatColor.RED + "An item you just attempted to purchase from the /ah, with IID " + itemID + ", has been flagged as a duplicated item. Your /bal has been refunded.");
                    economy.depositPlayer(Bukkit.getOfflinePlayer((UUID) player.getUniqueId()), (double) price);
                    economy.withdrawPlayer(Bukkit.getOfflinePlayer((UUID) listing.getWhoListed()), (double) price);
                    return;
                }
                ItemID.blacklistIID((long) itemID, (String) "auctionItemPurchase");
                item = ItemID.doesItemIDNeedToPersist((ItemStack) item) ? ItemID.setItemID((ItemStack) item, (long) ItemID.generateNewItemID()) : ItemID.removeItemID((ItemStack) item);
                player.getInventory().addItem(new ItemStack[]{item});
            } else {
                player.getInventory().addItem(new ItemStack[]{item});
            }
        } else {
            player.getInventory().addItem(new ItemStack[]{item});
        }
        DataHolder.removeListing(listing);
        DataHolder.removePlayerListing(listing.getWhoListed(), listing);
        Util.message(player, "&c&l- $" + price);
        player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 3.0f);
        Player seller = Bukkit.getPlayer((UUID) listing.getWhoListed());
        if (seller != null) {
            Util.message(seller, "&e&6" + player.getName() + "&e has &6purchased &eyour listing of &6" + Util.getItemName(item) + "&r&e!");
            Util.message(seller, "&a&l+ $" + price);
            seller.playSound(seller.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 0.65f);
            Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(seller, "AUCTION_SELL", new Object[]{item, player}));
        } else {
            Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(listing.getWhoListed(), "AUCTION_SELL", new Object[]{item, player}));
        }
        DataHandler.deleteListing(listing, true);
    }

    public static void attemptSell(Player player, ItemStack item, double price) {
        if (price > Double.MAX_VALUE) {
            player.sendMessage((Object) ChatColor.RED + "You cannot list for this amount.");
            return;
        }
        if (price < 0) {
            price = 0;
        }
        if (player.hasMetadata("auctionConfirm") && player.getMetadata("auctionConfirm").size() > 0) {
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object) ChatColor.RED + "You are creating auctions too quickly!");
            return;
        }
        double fee = Controller.getSellFee(player, price);
        if (!Auction.economy.has((OfflinePlayer) player, fee)) {
            player.sendMessage(CC.Red + "(!) " + CC.Red + "You cannot afford the sell fee of " + CC.RedB + "$" + CC.Red + NumberUtils.format((double) fee, (DecimalFormatType) DecimalFormatType.MONEY));
            return;
        }
        if (Auction.getPlugin().hasToggle(player)) {
            item = player.getItemInHand().clone();
            Controller.sell(player, item, price, fee, true);
        } else {
            item = player.getItemInHand().clone();
            player.setItemInHand(null);
            player.setMetadata("auctionConfirm", (MetadataValue) new FixedMetadataValue((Plugin) Auction.getPlugin(), (Object) item));
            player.setMetadata("auctionPrice", (MetadataValue) new FixedMetadataValue((Plugin) Auction.getPlugin(), (Object) price));
            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.8f, 0.8f);
            player.openInventory(ConfirmMenu.getConfirmMenu(item, price, fee, Controller.getSellFee(player)));
        }
    }

    public static double getSellFee(Player player, double cost) {
        if (Auction.getPlugin().getConfig().getBoolean("auction.sellFee", true)) {
            return Math.min(1000000.0, cost * Controller.getSellFee(player));
        }
        return 0.0;
    }

    public static double getSellFee(Player player) {

        if (player.hasPermission("enchantments.max.20")) {
            return 0.005;
        }
        if (player.hasPermission("enchantments.max.12")) {
            return 0.0075;
        }
        if (player.hasPermission("enchantments.max.9")) {
            return 0.01;
        }
        if (player.hasPermission("enchantments.max.6")) {
            return 0.0125;
        }
        return 0.015;
    }

    public static void sell(Player player, ItemStack item, double price, double fee, boolean removeItem) {
        Listing listing = new Listing(UUID.randomUUID(), AuctionUtil.getEndTime(), player.getUniqueId(), item, price);
        if (removeItem) {
            player.setItemInHand(null);
        }
        DataHolder.addListing(listing);
        DataHolder.addPlayerListing(player.getUniqueId(), listing);
        Util.message(player, "&e&l(!) &eYou have &6listed &eyour " + Util.getItemName(item) + "&e on the auction house for &6$" + price + "&e!");
        DataHandler.saveListing(listing, true);
        if (fee > 0.0) {
            Auction.economy.withdrawPlayer((OfflinePlayer) player, fee);
            player.sendMessage(CC.RedB + "(AH Fee) - $" + CC.Red + NumberUtils.format((double) fee, (DecimalFormatType) DecimalFormatType.MONEY));
        }
    }

    public static void cancel(Player player, Listing listing) {
        DataHolder.removeListing(listing);
        DataHolder.removePlayerListing(listing.getWhoListed(), listing);
        UnclaimedListing ucL = new UnclaimedListing(UUID.randomUUID(), listing.getWhoListed(), listing.getItem(), AuctionUtil.getUnclaimedEndTime());
        DataHolder.addUnclaimedListing(listing.getWhoListed(), ucL);
        DataHandler.deleteListing(listing, true);
        DataHandler.saveUnclaimedListing(ucL, true);
        Util.message(player, "&e&l(!) &eYou have &6cancelled &ean auction &6listing&e. Return cancelled and expired listings at &6/ah expired&e.");
    }

    public static void adminCancel(Player player, Listing listing) {
        DataHolder.removeListing(listing);
        DataHolder.removePlayerListing(listing.getWhoListed(), listing);
        UnclaimedListing ucL = new UnclaimedListing(UUID.randomUUID(), listing.getWhoListed(), listing.getItem(), AuctionUtil.getUnclaimedEndTime());
        if (Bukkit.getPlayer((UUID) listing.getWhoListed()) != null) {
            DataHolder.addUnclaimedListing(listing.getWhoListed(), ucL);
        }
        DataHandler.deleteListing(listing, true);
        DataHandler.saveUnclaimedListing(ucL, true);
        Util.message(player, "&c&l(!) &cListing removed.");
    }

    public static void expire(Listing listing) {
        DataHolder.removeListing(listing);
        DataHolder.removePlayerListing(listing.getWhoListed(), listing);
        UnclaimedListing ucL = new UnclaimedListing(UUID.randomUUID(), listing.getWhoListed(), listing.getItem(), AuctionUtil.getUnclaimedEndTime());
        if (Bukkit.getPlayer((UUID) listing.getWhoListed()) != null) {
            DataHolder.addUnclaimedListing(listing.getWhoListed(), ucL);
        }
        DataHandler.deleteListing(listing, true);
        DataHandler.saveUnclaimedListing(ucL, true);
    }

    public static void expireUnclaimed(UnclaimedListing listing) {
        DataHolder.removeUnclaimedListing(listing.getListedBy(), listing);
        DataHandler.deleteUnclaimedListing(listing, true);
    }

    public static void claim(Player player, UnclaimedListing listing) {
        DataHolder.getUnclaimedListings().get(player.getUniqueId()).remove(listing);
        DataHandler.deleteUnclaimedListing(listing, true);
        if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
            long itemID = ItemID.getItemID((ItemStack) listing.getItem());
            if (itemID != -1L) {
                if (ItemID.blacklist_iid.contains(itemID)) {
                    Bukkit.getLogger().info("[ItemID] Blacklisted IID #" + itemID + " found in /ah expired of player " + player.getName() + " item: " + (Object) listing.getItem());
                    player.sendMessage((Object) ChatColor.RED + "" + (Object) ChatColor.BOLD + "(!) " + (Object) ChatColor.RED + "An item you just attempted to claim from your Collection Bin, with IID " + itemID + ", has been flagged as a duplicated item, and has been removed.");
                    return;
                }
                ItemID.blacklistIID((long) itemID, (String) "expiredItemClaim");
                ItemStack listingItem = listing.getItem();
                if (ItemID.doesItemIDNeedToPersist((ItemStack) listingItem)) {
                    ItemID.setItemID((ItemStack) listingItem, (long) ItemID.generateNewItemID());
                } else {
                    ItemID.removeItemID((ItemStack) listingItem);
                }
                player.getInventory().addItem(new ItemStack[]{listingItem});
            } else {
                player.getInventory().addItem(new ItemStack[]{listing.getItem()});
            }
        } else {
            player.getInventory().addItem(new ItemStack[]{listing.getItem()});
        }
    }

    public static void claimAll(Player player) {
        int space = Util.getFreeInventorySpace(player);
        int i = 0;
        ArrayList<UnclaimedListing> claim = new ArrayList<UnclaimedListing>();
        for (UnclaimedListing listing : DataHolder.getUnclaimedListings().get(player.getUniqueId())) {
            if (i >= space && !Util.hasInventorySpace(player)) {
                Util.message(player, "&c&l(!) &cYou don't have enough inventory space.");
                break;
            }
            if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                long itemID = ItemID.getItemID((ItemStack) listing.getItem());
                if (itemID != -1L) {
                    if (ItemID.blacklist_iid.contains(itemID)) {
                        Bukkit.getLogger().info("[ItemID] Blacklisted IID #" + itemID + " found in /ah expired of player " + player.getName() + " item: " + (Object) listing.getItem());
                        player.sendMessage((Object) ChatColor.RED + "" + (Object) ChatColor.BOLD + "(!) " + (Object) ChatColor.RED + "An item you just attempted to claim from your Collection Bin, with IID " + itemID + ", has been flagged as a duplicated item, and has been removed.");
                        claim.add(listing);
                        ++i;
                        continue;
                    }
                    ItemID.blacklistIID((long) itemID, (String) "expiredItemClaimAll");
                    ItemStack listingItem = listing.getItem();
                    if (ItemID.doesItemIDNeedToPersist((ItemStack) listingItem)) {
                        ItemID.setItemID((ItemStack) listingItem, (long) ItemID.generateNewItemID());
                    } else {
                        ItemID.removeItemID((ItemStack) listingItem);
                    }
                    player.getInventory().addItem(new ItemStack[]{listingItem});
                } else {
                    player.getInventory().addItem(new ItemStack[]{listing.getItem()});
                }
            } else {
                player.getInventory().addItem(new ItemStack[]{listing.getItem()});
            }
            claim.add(listing);
            ++i;
        }
        for (UnclaimedListing listing : claim) {
            DataHolder.removeUnclaimedListing(player.getUniqueId(), listing);
            DataHandler.deleteUnclaimedListing(listing, true);
        }
        Util.message(player, "&e&l(!) &eYou have claimed &6" + i + " &e expired listings.");
    }

    public static void help(Player player) {
        Util.message(player, "");
        Util.message(player, "&e&l&nAuction House Commands");
        for (Command command : Command.values()) {
            Util.message(player, command.getDescription()[1]);
            Util.message(player, command.getDescription()[2]);
            Util.message(player, "");
        }
        player.sendMessage(CC.Yellow + "/ah startbid <startingBid> <bidIncrement> [buyNowPrice]");
        player.sendMessage(CC.Gray + "Create an auction package for others to bid on!");
        player.sendMessage("");
        player.sendMessage(CC.Yellow + "/ah collect");
        player.sendMessage(CC.Gray + "Collect items from previous bid auctions!");
        player.sendMessage("");
        player.sendMessage(CC.Yellow + "/ah bids");
        player.sendMessage(CC.Gray + "View all active bid auctions!");
        player.sendMessage("");
        player.sendMessage(CC.Yellow + "/ah stfu");
        player.sendMessage(CC.Gray + "Turn off alerts when someone outbids you!");
        player.sendMessage("");
    }

    public static void expireListings() {
        new BukkitRunnable() {

            public void run() {
                if (Auction.stoppingServer || Arkkit.get().shutting_down || CommandArkReboot.restart_task_exists) {
                    return;
                }
                List<Listing> listings = DataHolder.getListings();
                if (listings.size() > 0 && listings.get(listings.size() - 1).getEndTime() < System.currentTimeMillis()) {
                    Listing l = listings.get(listings.size() - 1);
                    Controller.expire(l);
                    Bukkit.getLogger().info("[Auction] Expiring auction " + l.getId() + " of " + (Object) l.getItem() + " for " + l.getWhoListed() + " for $" + l.getPrice());
                }
            }
        }.runTaskTimer((Plugin) Auction.plugin, 100L, 100L);
    }

    public static void expireOfflineUnclaimedListings() {
        new BukkitRunnable() {

            public void run() {
                DataHandler.deleteOverExpiredUnclaimedListing(true);
            }
        }.runTaskTimer((Plugin) Auction.plugin, 0L, 3600L);
    }
}

