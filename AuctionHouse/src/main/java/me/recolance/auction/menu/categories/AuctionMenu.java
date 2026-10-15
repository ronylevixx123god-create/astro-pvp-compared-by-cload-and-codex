/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package me.recolance.auction.menu.categories;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.controller.Controller;
import me.recolance.auction.data.DataHolder;
import me.recolance.auction.menu.MenuRenderer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public abstract class AuctionMenu
        extends CosmicGUI {
    public long nextClickExpiration = System.currentTimeMillis() + 1000L;

    public AuctionMenu(Player player, String title, int size) {
        super(player, title, size);
    }

    public void clearItems() {
        this.inventory.clear();
        this.getInventoryItems().clear();
    }

    public InventoryItem getAuctionItem(Listing item) {
        Runnable leftClick = () -> {
            if (!this.canClick()) {
                return;
            }
            if (!item.isValid() || item.isExpired()) {
                this.player.sendMessage(CC.Red + "Item no longer available!");
                this.redraw();
                return;
            }
            if (item.getWhoListed().equals(this.player.getUniqueId())) {
                this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot purchase your own item!");
                this.player.sendMessage(CC.Gray + "Shift Right-Click to stop selling this item!");
                return;
            }
            this.nextClickExpiration = System.currentTimeMillis() + 100L;
            this.closeWithDelay(pl -> MenuRenderer.purchaseConfirmation(this.player, item, true));
        };
        return new InventoryItem(new ItemBuilder(item.getMenuDisplayItem()).build()).click(ClickType.LEFT, leftClick).click(ClickType.SHIFT_RIGHT, () -> {
            if (!this.canClick()) {
                return;
            }
            if (!item.isValid() || item.isExpired()) {
                this.player.sendMessage(CC.Red + "Item no longer valid!");
                this.redraw();
                return;
            }
            if (item.getWhoListed().equals(this.player.getUniqueId())) {
                this.nextClickExpiration = System.currentTimeMillis() + 100L;
                if (item.isValid() && !item.isExpired()) {
                    Controller.cancel(this.player, item);
                    this.redraw();
                }
            } else {
                leftClick.run();
            }
        });
    }

    public InventoryItem getCollectionBinItem() {
        List<UnclaimedListing> itemsSelling = DataHolder.getUnclaimedListings().get(this.player.getUniqueId());
        int emptyItems = itemsSelling != null ? (int) itemsSelling.stream().filter(item -> !item.isExpired()).count() : 0;
        return new InventoryItem(new ItemBuilder(Material.ENDER_CHEST, CC.YellowB + "Collection Bin", new String[]{CC.Gray + "Click to view and collect", CC.Gray + "all of your active, cancelled", CC.Gray + "or expired Auction items.", "", CC.YellowB + emptyItems + " Item" + (emptyItems != 1 ? "s" : "")}).build()).click(() -> this.closeWithDelay(pl -> MenuRenderer.expired(this.player)));
    }

    public boolean canClick() {
        if (this.nextClickExpiration > System.currentTimeMillis() && !this.player.hasPermission("cosmicsky.auction.vip")) {
            this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Please slow down in between clicks!");
            return false;
        }
        return true;
    }
}

