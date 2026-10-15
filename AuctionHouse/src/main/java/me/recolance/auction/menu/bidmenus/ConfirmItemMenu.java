/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import me.recolance.auction.Auction;
import me.recolance.auction.data.AuctionBid;
import me.recolance.auction.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class ConfirmItemMenu
        extends CosmicGUI {
    Consumer<Void> confirmCallback;
    Consumer<Void> denyCallback;
    private AuctionBid bid;
    private long bidding;

    public ConfirmItemMenu(Player player, String title, long bidding, AuctionBid bid, Consumer<Void> confirm, Consumer<Void> deny) {
        super(player, title, 9);
        this.confirmCallback = confirm;
        this.denyCallback = deny;
        this.bid = bid;
        this.bidding = bidding;
        if (Auction.getPlugin().getToggles().isToggled(player)) {
            Bukkit.getLogger().info("Automatically accepting confirm menu due to safetey toggles turned off: " + player.getName());
            confirm.accept(null);
        }
    }

    public static Consumer<Void> getConfirmCallback(AuctionBid bid, Player pl, long finalBidAmount, boolean buyout) {
        return confirm -> {
            if (bid.isExpired()) {
                pl.sendMessage(CC.RedB + "Item no longer available!");
                return;
            }
            if (bid.getCurrentBid() >= finalBidAmount) {
                pl.sendMessage((Object) ChatColor.RED + "Your bid must be higher than the current bid!");
                pl.sendMessage(CC.RedB + "Current Bid: $" + CC.Red + Util.formatMoney(bid.getCurrentBid()));
                return;
            }
            if (!Auction.economy.has((OfflinePlayer) pl, (double) finalBidAmount)) {
                pl.sendMessage(CC.Red + "You do not have the funds to do that!");
                return;
            }
            if (buyout) {
                bid.buyNow(pl);
            } else {
                bid.placePlayerBid(pl, finalBidAmount);
            }
            pl.closeInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> new BidMenu(pl).openGUI(Auction.getPlugin()), 1L);
        };
    }

    public void redraw() {
        int i;
        boolean buyout = this.bid.getBuyoutAmount() > 0.0 && this.bid.getBuyoutAmount() == (double) this.bidding;
        InventoryItem item = new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE, CC.GreenB + "Confirm " + (buyout ? "Buy" : "Bid") + " Now", new String[]{(Object) ChatColor.GRAY + "Click to confirm " + (buyout ? "purchase of" : "bid on"), CC.Gray + "'" + CC.White + this.bid.getBidSellDisplayName() + CC.Gray + "' for " + CC.GreenB + "$" + CC.Green + Util.formatMoney(this.bidding)}).data((short) DyeColor.LIME.getWoolData()).build()).click(() -> this.confirmCallback.accept(null));
        InventoryItem deny = new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE, CC.RedB + "Cancel", new String[]{(Object) ChatColor.GRAY + "Click to cancel this bid!"}).data((short) DyeColor.RED.getWoolData()).build()).click(() -> this.denyCallback.accept(null));
        for (i = 0; i < 4; ++i) {
            this.setItem(i, item);
        }
        for (i = 4; i < this.size; ++i) {
            this.setItem(i, deny);
        }
        this.setItem(4, new InventoryItem(this.bid.getSellingItems().get(0).clone()));
    }
}

