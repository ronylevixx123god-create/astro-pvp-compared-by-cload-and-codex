/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.google.common.collect.Lists
 *  net.minecraft.util.io.netty.util.internal.ConcurrentSet
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import io.netty.util.internal.ConcurrentSet;
import me.recolance.auction.Auction;
import me.recolance.auction.data.AuctionBid;
import me.recolance.auction.menu.BidManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BidMenu
        extends BiddableMenu {
    int page = 1;
    private long lastUpdate = 0L;
    private boolean forceUpdate = false;

    public BidMenu(Player player) {
        super(player, "Active Bids", 54);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    public void redraw() {
        int startIndex;
        if (System.currentTimeMillis() - this.lastUpdate <= 500L && !this.forceUpdate) {
            return;
        }
        this.forceUpdate = false;
        BidManager bidManager = Auction.getPlugin().getBidManager();
        ArrayList<Map.Entry<UUID, AuctionBid>> bidEntries = Lists.newArrayList(bidManager.getActiveAuctionBids().entrySet()); // HELP
        bidEntries.sort((o1, o2) -> {
            int secondsLeft2;
            int secondsLeft1 = o1.getValue().getSecondsLeftInAuction();
            if (secondsLeft1 > (secondsLeft2 = o2.getValue().getSecondsLeftInAuction())) {
                return -1;
            }
            if (secondsLeft2 == secondsLeft1) {
                return 0;
            }
            return 1;
        });
        double perPage = 45.0;
        int maxPages = Math.max(1, (int) Math.ceil((double) bidEntries.size() / perPage));
        if (this.page < 1) {
            this.page = 1;
        }
        if (this.page > maxPages) {
            this.page = maxPages;
        }
        this.inventory.clear();
        this.getInventoryItems().clear();
        this.lastUpdate = System.currentTimeMillis();
        AtomicInteger slot = new AtomicInteger(0);
        for (int i = startIndex = (int) ((double) this.page * perPage - perPage); (double) i < (double) startIndex + perPage && i < bidEntries.size(); ++i) {
            Map.Entry entry = bidEntries.get(i);
            AuctionBid auctionBid = (AuctionBid) entry.getValue();
            if (slot.get() >= this.size) break;
            if (auctionBid.isExpired()) continue;
            this.setItem(slot.getAndIncrement(), new InventoryItem(new ItemBuilder(auctionBid.getAuctionDisplayItem(true)).build()).click(ClickType.LEFT, auctionBid.getLeftClickLogic(this, this.player)).click(ClickType.RIGHT, auctionBid.getRightClickLogic(this, this.player)).click(ClickType.MIDDLE, auctionBid.getMiddleClickLogic(this, this.player)).click(ClickType.SHIFT_LEFT, auctionBid.getShiftLeftClickLogic(this, this.player)).click(ClickType.SHIFT_RIGHT, () -> {
                Set watching = Auction.getPlugin().getBidManager().getWatchlists().computeIfAbsent(this.player.getUniqueId(), list -> new ConcurrentSet());
                if (watching.contains(auctionBid.getBidID())) {
                    watching.remove(auctionBid.getBidID());
                    this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Item removed from watchlist!");
                    if (watching.isEmpty()) {
                        Auction.getPlugin().getBidManager().getWatchlists().remove(this.player.getUniqueId());
                    }
                    return;
                }
                if (watching.size() >= 40) {
                    this.player.sendMessage(CC.RedB + "Your watch list is currently full!");
                    this.player.sendMessage(CC.Gray + "Visit the watchlist tab to remove expired bids!");
                    return;
                }
                this.player.sendMessage(CC.GreenB + "(!) " + CC.Green + "Item added to your bidding watchlist!");
                watching.add(auctionBid.getBidID());
                this.player.playSound(this.player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.4f);
            }).click(ClickType.NUMBER_KEY, () -> this.forceExpireAuction(auctionBid, this.player)));
        }
        this.setItem(this.size - 9, new InventoryItem(new ItemBuilder(Material.CHEST, CC.YellowB + "Return to Auction", CC.Gray + "Click here to return to the", CC.Gray + "auction house main menu.").build()).click(() -> {
            this.player.closeInventory();
            this.player.performCommand("ah");
        }));
        this.setItem(this.size - 8, new InventoryItem(new ItemBuilder(Material.DIAMOND, CC.AquaB + "View Watchlist").lore(CC.Gray + "Click here to view all active", CC.Gray + "bids on your watchlist!").build()).click(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(Auction.getPlugin(), () -> new WatchlistMenu(this.player).openGUI(Auction.getPlugin()))));
        this.setItem(this.size - 6, new InventoryItem(new ItemBuilder(Material.PAPER, CC.YellowB + "<- Previous Page", CC.Gray + "View previous page of auction biddings.").build()).click(() -> {
            --this.page;
            this.forceUpdate = true;
            this.redraw();
        }));
        this.setItem(this.size - 4, new InventoryItem(new ItemBuilder(Material.PAPER, CC.YellowB + "Next Page ->", CC.Gray + "View next page of auction biddings.").build()).click(() -> {
            ++this.page;
            this.forceUpdate = true;
            this.redraw();
        }));
    }

    public void forceExpireAuction(AuctionBid auctionBid, Player player) {
        if (!player.isOp()) {
            return;
        }
        if (auctionBid.isExpired()) {
            player.sendMessage(ChatColor.RED + "Bid already expired!");
            return;
        }
        auctionBid.expire();
        if (auctionBid.getCurrentHighestBidder() != null) {
            OfflinePlayer offlineBuyer = Bukkit.getOfflinePlayer(auctionBid.getCurrentHighestBidder());
            if (offlineBuyer != null) {
                Auction.economy.depositPlayer(offlineBuyer, (double) auctionBid.getCurrentBid());
            }
            player.sendMessage(CC.Red + "Returned " + auctionBid.getCurrentBidderName() + " $" + auctionBid.getCurrentBid());
        }
        player.sendMessage(CC.RedB + "Auction Bid force expired and items have been returned to seller!");
        this.redraw();
    }
}

