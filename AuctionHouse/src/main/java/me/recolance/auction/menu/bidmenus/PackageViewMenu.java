/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.InventoryUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.InventoryUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import me.recolance.auction.Auction;
import me.recolance.auction.data.AuctionBid;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PackageViewMenu
        extends CosmicGUI {
    private AuctionBid toView;
    private BiddableMenu menu;

    public PackageViewMenu(Player player, AuctionBid toView, BiddableMenu parent) {
        super(player, toView.getSellerName() + "'s Package Items", InventoryUtils.fitSize((int) (toView.getSellingItems().size() + 1)));
        this.toView = toView;
        this.menu = parent;
    }

    public void redraw() {
        if (this.toView.isExpired()) {
            this.player.closeInventory();
            this.player.sendMessage(CC.Red + "That Auction item is no longer available!");
            return;
        }
        int slot = 0;
        for (ItemStack item : this.toView.getSellingItems()) {
            this.setItem(slot++, item, null);
        }
        this.setItem(this.size - 1, new InventoryItem(new ItemBuilder(Material.ARROW).name(CC.GreenB + "Return to Auctions").lore(new String[]{"", CC.Gray + "Click to return to auction bids!"}).build()).click(() -> Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Auction.getPlugin(), () -> this.menu.openGUI(Auction.getPlugin()), 1L)));
    }
}

