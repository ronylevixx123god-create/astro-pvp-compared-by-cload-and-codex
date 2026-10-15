/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  net.minecraft.util.io.netty.util.internal.ConcurrentSet
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import io.netty.util.internal.ConcurrentSet;
import me.recolance.auction.Auction;
import me.recolance.auction.data.AuctionBid;
import me.recolance.auction.menu.BidManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

public class WatchlistMenu
        extends BiddableMenu {
    private LinkedList<AuctionBid> bidsWatching = new LinkedList();

    public WatchlistMenu(Player player) {
        super(player, "Bidding Watchlist", 54);
        BidManager bidManager = Auction.getPlugin().getBidManager();
        ConcurrentSet<UUID> bids = bidManager.getWatchlists().get(player.getUniqueId());
        if (bids != null) {
            bids.forEach(bidId -> {
                AuctionBid bid = bidManager.getActiveAuctionBids().get(bidId);
                if (bid == null || bid.isExpired()) {
                    bids.remove(bidId);
                    return;
                }
                if (!this.bidsWatching.contains(bid)) {
                    this.bidsWatching.add(bid);
                }
            });
            if (bids.isEmpty()) {
                bidManager.getWatchlists().remove(player.getUniqueId());
            }
        }
        if (this.bidsWatching != null) {
            this.bidsWatching.sort((o1, o2) -> {
                int secondsLeft2;
                int secondsLeft1 = o1.getSecondsLeftInAuction();
                if (secondsLeft1 > (secondsLeft2 = o2.getSecondsLeftInAuction())) {
                    return -1;
                }
                if (secondsLeft2 == secondsLeft1) {
                    return 0;
                }
                return 1;
            });
        }
    }

    public void redraw() {
        int slot = 0;
        for (AuctionBid bid : this.bidsWatching) {
            ItemBuilder builder = new ItemBuilder(bid.getAuctionDisplayItem(false));
            if (bid.isExpired()) {
                builder.addLoreLine("", CC.RedB + "EXPIRED", bid.getCurrentHighestBidder() != null ? CC.GreenB + "BUYER: " + CC.Green + bid.getCurrentBidderName() : "");
            }
            this.setItem(slot++, new InventoryItem(builder.build()).click(ClickType.LEFT, () -> {
                if (bid.isExpired()) {
                    this.player.sendMessage(CC.RedB + "That auction has expired!");
                    this.player.sendMessage(CC.Gray + "Shift-RClick to remove this from your watchlist!");
                    return;
                }
                bid.getLeftClickLogic(this, this.player).run();
            }).click(ClickType.RIGHT, () -> {
                if (bid.isExpired()) {
                    this.player.sendMessage(CC.RedB + "That auction has expired!");
                    this.player.sendMessage(CC.Gray + "Shift-RClick to remove this from your watchlist!");
                    return;
                }
                bid.getRightClickLogic(this, this.player).run();
            }).click(ClickType.MIDDLE, () -> {
                if (bid.isExpired()) {
                    this.player.sendMessage(CC.RedB + "That auction has expired!");
                    this.player.sendMessage(CC.Gray + "Shift-RClick to remove this from your watchlist!");
                    return;
                }
                bid.getMiddleClickLogic(this, this.player).run();
            }).click(ClickType.SHIFT_LEFT, () -> {
                if (bid.isExpired()) {
                    this.player.sendMessage(CC.RedB + "That auction has expired!");
                    this.player.sendMessage(CC.Gray + "Shift-RClick to remove this from your watchlist!");
                    return;
                }
                bid.getShiftLeftClickLogic(this, this.player).run();
            }).click(ClickType.SHIFT_RIGHT, () -> {
                Set watching = (Set) Auction.getPlugin().getBidManager().getWatchlists().computeIfAbsent(this.player.getUniqueId(), list -> new ConcurrentSet());
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Item removed from watchlist!");
                watching.remove(bid.getBidID());
                this.bidsWatching.remove(bid);
                if (watching.isEmpty()) {
                    Auction.getPlugin().getBidManager().getWatchlists().remove(this.player.getUniqueId());
                }
                this.redraw();
            }));
        }
        this.setItem(this.size - 9, new InventoryItem(new ItemBuilder(Material.CHEST, CC.YellowB + "Return to Bids", CC.Gray + "Click here to return to the", CC.Gray + "bid auction main page.").build()).click(() -> Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> new BidMenu(this.player).openGUI(Auction.getPlugin()))));
    }
}

