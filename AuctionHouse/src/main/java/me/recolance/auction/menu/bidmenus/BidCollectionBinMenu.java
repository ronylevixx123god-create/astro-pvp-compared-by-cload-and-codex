/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.InventoryUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.recolance.auction.menu.bidmenus;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.InventoryUtils;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.recolance.auction.Auction;
import me.recolance.auction.menu.BidManager;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BidCollectionBinMenu
        extends CosmicGUI {
    public BidCollectionBinMenu(Player player, List<ItemStack> bin) {
        super(player, "Auction Bid Collection", InventoryUtils.fitSize((int) (bin != null ? bin.size() + 1 : 9)));
    }

    public void redraw() {
        BidManager bidManager = Auction.getPlugin().getBidManager();
        List<ItemStack> binItems = bidManager.getBidCollectionBin().get(this.player.getUniqueId());
        this.inventory.clear();
        this.getInventoryItems().clear();
        if (binItems != null && !binItems.isEmpty()) {
            int slot = 0;
            for (ItemStack toClaim : binItems) {
                if (slot >= this.size) break;
                this.setItem(slot++, new InventoryItem(toClaim).click(() -> {
                    if (!binItems.contains((Object) toClaim)) {
                        this.redraw();
                        return;
                    }
                    if (this.player.getInventory().firstEmpty() == -1) {
                        this.player.sendMessage((Object) ChatColor.RED + "You do not have the inventory space for this item!");
                        return;
                    }
                    binItems.remove((Object) toClaim);
                    if (binItems.isEmpty()) {
                        bidManager.getBidCollectionBin().remove(this.player.getUniqueId());
                    }
                    if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                        Bukkit.getLogger().info("[AuctionHouse] " + this.player.getName() + " has claimed their auction winning of " + (Object) toClaim + " with IID: " + ItemID.getItemID((ItemStack) toClaim));
                    }
                    PlayerUtil.giveOrDropProtectedItem((Player) this.player, (ItemStack) toClaim, (int) -1);
                    if (binItems.isEmpty()) {
                        new BidMenu(this.player).openGUI(Auction.getPlugin());
                        return;
                    }
                    this.redraw();
                }));
            }
        }
        this.setItem(this.size - 1, new InventoryItem(new ItemBuilder(Material.ARROW).name(CC.GreenB + "Return to Auction Page").lore(new String[]{CC.Gray + "Click here to return to the", CC.Gray + "auction house main menu."}).build()).click(() -> {
            this.player.closeInventory();
            this.player.performCommand("ah");
        }));
    }
}

