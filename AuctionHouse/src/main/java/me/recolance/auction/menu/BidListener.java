/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.recolance.auction.Auction;
import me.recolance.auction.data.AuctionBid;
import me.recolance.auction.menu.bidmenus.BidMenu;
import me.recolance.auction.menu.bidmenus.ConfirmItemMenu;
import me.recolance.auction.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class BidListener
        implements Listener {
    private static String auctionBiddingMetadata = "auctionBidding";

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals("Active Bids") || event.getInventory().getName().equals("Enter Auction Package Items") || event.getInventory().getName().endsWith("'s Package Items") || event.getInventory().getName().equals("Auction Bid Collection") || event.getInventory().getName().equals("Confirm Purchase Now") || event.getInventory().getName().equals("Confirm Bid Now") || event.getInventory().getName().startsWith("Buy It Now") || event.getInventory().getName().equals("Confirm First Time Bid") || event.getInventory().getName().equals("Bidding Watchlist")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player clicked;
        if (event.getRightClicked() instanceof Player && (clicked = (Player) event.getRightClicked()).getName().endsWith("Auctioneer") && clicked.hasMetadata("NPC")) {
            event.setCancelled(true);
            event.getPlayer().chat("/ah");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Auction.getPlugin().getBidManager().setProccessAuctionHouse(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPLayerChatEvent(AsyncPlayerChatEvent event) {
        Player pl = event.getPlayer();
        if (pl.hasMetadata(auctionBiddingMetadata)) {
            event.setCancelled(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> {
                boolean buyout;
                UUID uuid = UUID.fromString(((MetadataValue) pl.getMetadata(auctionBiddingMetadata).get(0)).asString());
                if (uuid == null) {
                    pl.removeMetadata(auctionBiddingMetadata, (Plugin) Auction.getPlugin());
                    pl.sendMessage(CC.RedB + "Auction no longer valid!");
                    return;
                }
                String msg = ChatColor.stripColor((String) event.getMessage());
                BidManager bidManager = Auction.getPlugin().getBidManager();
                AuctionBid bid = bidManager.getActiveAuctionBids().get(uuid);
                if (bid == null || bid.isExpired()) {
                    pl.sendMessage((Object) ChatColor.RED + "Auction no longer valid!");
                    pl.removeMetadata(auctionBiddingMetadata, (Plugin) Auction.getPlugin());
                    return;
                }
                if (msg.equalsIgnoreCase("cancel")) {
                    pl.sendMessage((Object) ChatColor.RED + "Auction bid cancelled!");
                    pl.removeMetadata(auctionBiddingMetadata, (Plugin) Auction.getPlugin());
                    return;
                }
                Long bidAmount = Util.parseLongOrNull(msg);
                if (bidAmount == null) {
                    try {
                        bidAmount = Util.getMoneyValue(msg);
                    } catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (bidAmount == null || bidAmount <= 0L) {
                    pl.sendMessage(CC.Red + "You must enter a valid bid!");
                    return;
                }
                if (bid.getCurrentBid() >= bidAmount) {
                    pl.sendMessage((Object) ChatColor.RED + "Your bid must be higher than the current bid!");
                    pl.sendMessage(CC.RedB + "Current Bid: $" + CC.Red + Util.formatMoney(bid.getCurrentBid()));
                    pl.sendMessage((Object) ChatColor.GRAY + "Type '" + CC.Red + "cancel" + CC.Gray + "' to cancel this auction bid!");
                    return;
                }
                if ((double) bid.getCurrentBid() < bid.getStartingBid() && (double) bidAmount.longValue() < bid.getStartingBid()) {
                    pl.sendMessage(CC.Red + "You must bid atleast the starting bid amount!");
                    pl.sendMessage(CC.RedB + "Starting Bid: $" + CC.Red + Util.formatMoney(bid.getStartingBid()));
                    return;
                }
                if (!Auction.economy.has((OfflinePlayer) pl, (double) bidAmount.longValue())) {
                    pl.sendMessage(CC.Red + "You cannot afford that bid!");
                    pl.sendMessage(CC.Red + "Current Balance: " + CC.RedB + "$" + CC.Red + Util.formatMoney(Auction.economy.getBalance((OfflinePlayer) pl)));
                    return;
                }
                long nextBid = bid.getCurrentBid() + bid.getBidIncrementAmount();
                if (nextBid > bidAmount) {
                    pl.sendMessage((Object) ChatColor.RED + "Your bid must be higher than the current bid with the bid increment!");
                    pl.sendMessage(CC.RedB + "Required Bid: $" + CC.Red + Util.formatMoney(nextBid));
                    return;
                }
                boolean bl = buyout = bid.getBuyoutAmount() > 0.0 && (double) bidAmount.longValue() == bid.getBuyoutAmount();
                if (buyout && (double) bid.getCurrentBid() >= bid.getBuyoutAmount()) {
                    pl.sendMessage(CC.RedB + "Buyout no longer available for that item!");
                    pl.removeMetadata(auctionBiddingMetadata, (Plugin) Auction.getPlugin());
                    return;
                }
                pl.removeMetadata(auctionBiddingMetadata, (Plugin) Auction.getPlugin());
                Long finalBidAmount = bidAmount;
                new ConfirmItemMenu(pl, "Confirm " + (buyout ? "Purchase" : "Bid") + " Now", finalBidAmount, bid, ConfirmItemMenu.getConfirmCallback(bid, pl, finalBidAmount, buyout), deny -> new BidMenu(pl).openGUI(Auction.getPlugin())).openGUI(Auction.getPlugin());
            });
        }
    }
}

