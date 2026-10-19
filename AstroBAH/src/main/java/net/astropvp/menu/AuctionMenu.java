//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.menu;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.InventoryHolder;
import net.astropvp.auction.Auction;
import org.bukkit.Bukkit;
import net.astropvp.util.Utils;
import net.astropvp.auction.AuctionManager;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import java.util.Arrays;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

public class AuctionMenu implements Menu
{
    private Inventory inventory;

    public static ItemStack getCollectionBinItem() {
        final ItemStack item = new ItemStack(Material.ENDER_CHEST);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&b&lCollection Bin"));
        meta.setLore(new ArrayList(Collections.singletonList(CC.translate("&7Click to view your collection bin."))));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getHelpItem() {
        final ItemStack item = new ItemStack(Material.BOOK);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&b&lBlack Market Auction"));
        meta.setLore((List)new ArrayList(Arrays.asList(CC.translate("&7Every 30 minutes, a &nrandom rare item &7will be"), CC.translate("&7listed on the /bah for players to bid on."), CC.translate(""), CC.translate("&7The bid increments will &nincrease as the"), CC.translate("&7&nmax bid increases,&7 with a &f10s anti-snipe"), CC.translate("&ftimer&7 added each time a bid is placed."), CC.translate(""), CC.translate("&7The player with the highest bid at the"), CC.translate("&7end of the auction will recieve the item."))));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCurrentAuctionItem(final boolean withoutEnding) {
        if (BlackMarketAuction.get().getAuctionManager().getCurrentAuction() == null) {
            final ItemStack item = new ItemStack(Material.BARRIER);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(CC.translate("&c&l???"));
            String timeLeft = "???";
            meta.setLore((List)new ArrayList(Collections.singletonList(CC.translate("&cNext Auction in &n" + timeLeft + "&c..."))));
            item.setItemMeta(meta);
            return item;
        }
        final ItemStack item = AuctionManager.get().getCurrentAuction().item.clone();
        final ItemMeta meta = item.getItemMeta();
        final List<String> lore = (item.hasItemMeta() && meta.hasLore()) ? meta.getLore() : new ArrayList<String>();
        final Auction auction = AuctionManager.get().getCurrentAuction();
        lore.add("");
        lore.add(CC.translate("&7&m-----------------------"));
        lore.add(CC.translate("&b&lCLICK &7item to bid &a&l$&a" + Utils.formatMoney(auction.currentBid + auction.bidIncrement) + "!"));
        lore.add("");
        lore.add(CC.translate("&bCurrent Bid: &f&l$&f" + Utils.formatMoney(auction.currentBid)));
        lore.add(CC.translate("&bHighest Bidder: " + ((auction.currentTopBidder == null) ? "&cNone" : ("&f" + auction.topBidderName))));
        lore.add(CC.translate("&bBid Increment: &f&l$&f" + Utils.formatMoney(auction.bidIncrement)));
        if (!withoutEnding) {
            lore.add(CC.translate("&bBidding ends in: &f" + TimeUtils.formatFutureTime(auction.endsAt)));
        }
        lore.add(CC.translate("&7&m-----------------------"));
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return item;
    }

    public AuctionMenu() {
        (this.inventory = Bukkit.createInventory((InventoryHolder)this, 9, "Black Market Auction")).setItem(0, getCollectionBinItem());
        this.inventory.setItem(1, Utils.getFillerItem());
        this.inventory.setItem(2, Utils.getFillerItem());
        this.inventory.setItem(3, Utils.getFillerItem());
        this.inventory.setItem(4, getCurrentAuctionItem(false));
        this.inventory.setItem(5, Utils.getFillerItem());
        this.inventory.setItem(6, Utils.getFillerItem());
        ItemStack history = new ItemStack(Material.BOOK);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName(CC.translate("&b&lAuction History"));
        history.setItemMeta(historyMeta);
        this.inventory.setItem(7, history);
        this.inventory.setItem(8, getHelpItem());
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BlackMarketAuction.get(), () -> this.inventory.setItem(4, getCurrentAuctionItem(false)), 0L, 20L);
    }

    public void forceUpdate() {
        this.inventory.setItem(4, getCurrentAuctionItem(false));
    }

    @Override
    public void onClick(final Player player, final InventoryClickEvent event) {
        event.setCancelled(true);
        final int slot = event.getRawSlot();
        if (slot == 0) {
            Bukkit.dispatchCommand((CommandSender)player, "ah expired bah");
        }
        else if (slot == 7) {
            player.openInventory(new AuctionHistoryMenu(1).getInventory());
        }
        else if (slot == 4 && AuctionManager.get().getCurrentAuction() != null) {
                if (AuctionManager.get().getCurrentAuction() == null || !AuctionManager.get().getCurrentAuction().auctionUUID.equals(BlackMarketAuction.get().getAuctionManager().getCurrentAuction().auctionUUID)) {
                    player.sendMessage(CC.translate("&c&l(!) &cThis auction has already ended."));
                    return;
                }
                if (AuctionManager.get().getCurrentAuction().currentTopBidder != null && AuctionManager.get().getCurrentAuction().currentTopBidder.equals(player.getUniqueId())) {
                    player.sendMessage(CC.translate("&c&l(!) &cYou are already the highest bidder on this item."));
                    player.sendMessage(CC.translate("&7You must wait for another player to outbid your current bid."));
                    return;
                }
                final Auction currentAuction = AuctionManager.get().getCurrentAuction();
                currentAuction.doBid(player, AuctionManager.get().getCurrentAuction().currentBid + AuctionManager.get().getCurrentAuction().bidIncrement);
                BlackMarketAuction.get().getAuctionMenu().forceUpdate();
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
