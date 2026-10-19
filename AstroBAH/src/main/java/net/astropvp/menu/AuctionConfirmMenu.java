//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.menu;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import net.astropvp.auction.AuctionManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import net.astropvp.util.Utils;
import org.bukkit.ChatColor;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import net.astropvp.auction.Auction;

public class AuctionConfirmMenu implements Menu
{
    private Auction auction;
    private Inventory inventory;
    private ItemStack auctionItem;
    private ItemStack realItem;
    private double amount;

    public static ItemStack getConfirmItem(final ItemStack item, final double amount) {
        final ItemStack confirmItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
        final ItemMeta meta = confirmItem.getItemMeta();
        meta.setDisplayName(CC.translate("&a&lConfirm Bid"));
        meta.setLore((List)new ArrayList(Arrays.asList(CC.translate("&7Confirm bid on"), ChatColor.RESET.toString() + ChatColor.WHITE + ((item.hasItemMeta() && item.getItemMeta().hasLore()) ? item.getItemMeta().getDisplayName() : (item.getType().toString() + CC.translate(" &7for"))), CC.translate("&a&l$&a" + Utils.formatMoney(amount) + "!"))));
        confirmItem.setItemMeta(meta);
        return confirmItem;
    }

    public static ItemStack getCancelItem(final ItemStack item, final double amount) {
        final ItemStack confirmItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
        final ItemMeta meta = confirmItem.getItemMeta();
        meta.setDisplayName(CC.translate("&c&lCancel Bid"));
        meta.setLore((List)new ArrayList(Arrays.asList(CC.translate("&7Cancel bid of"), ChatColor.RESET.toString() + ChatColor.WHITE + ((item.hasItemMeta() && item.getItemMeta().hasLore()) ? item.getItemMeta().getDisplayName() : (item.getType().toString() + CC.translate(" &7for"))), CC.translate("&c&l$&c" + Utils.formatMoney(amount) + "!"))));
        confirmItem.setItemMeta(meta);
        return confirmItem;
    }

    public AuctionConfirmMenu(final Auction auction, final ItemStack auctionItem, final ItemStack item, final double amount) {
        this.auction = auction;
        this.inventory = Bukkit.createInventory((InventoryHolder)this, 9, "Bid of $" + Utils.formatMoney(amount));
        this.amount = amount;
        this.auctionItem = auctionItem;
        this.realItem = item;
        this.inventory.setItem(0, getConfirmItem(this.realItem, amount));
        this.inventory.setItem(1, getConfirmItem(this.realItem, amount));
        this.inventory.setItem(2, getConfirmItem(this.realItem, amount));
        this.inventory.setItem(3, getConfirmItem(this.realItem, amount));
        this.inventory.setItem(4, auctionItem);
        this.inventory.setItem(5, getCancelItem(this.realItem, amount));
        this.inventory.setItem(6, getCancelItem(this.realItem, amount));
        this.inventory.setItem(7, getCancelItem(this.realItem, amount));
        this.inventory.setItem(8, getCancelItem(this.realItem, amount));
    }

    @Override
    public void onClick(final Player player, final InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getRawSlot() >= 0 && event.getRawSlot() <= 4) {
            if (AuctionManager.get().getCurrentAuction() == null || !AuctionManager.get().getCurrentAuction().auctionUUID.equals(this.auction.auctionUUID)) {
                player.sendMessage(CC.translate("&c&l(!) &cThis auction has already ended."));
                return;
            }
            if (AuctionManager.get().getCurrentAuction().currentTopBidder != null && AuctionManager.get().getCurrentAuction().currentTopBidder.equals(player.getUniqueId())) {
                player.sendMessage(CC.translate("&c&l(!) &cYou are already the highest bidder on this item."));
                player.sendMessage(CC.translate("&7You must wait for another player to outbid your current bid."));
                return;
            }
            final Auction currentAuction = AuctionManager.get().getCurrentAuction();
            currentAuction.doBid(player, this.amount);
            player.openInventory(BlackMarketAuction.get().getAuctionMenu().getInventory());
            BlackMarketAuction.get().getAuctionMenu().forceUpdate();
        }
        else if (event.getRawSlot() >= 5 && event.getSlot() <= 8) {
            player.openInventory(BlackMarketAuction.get().getAuctionMenu().getInventory());
        }
    }

    @Override
    public void onOpen(final Player player) {
    }

    @Override
    public void onClose(final Player player) {
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
