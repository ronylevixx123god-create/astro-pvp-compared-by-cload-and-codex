// Decompiled with: CFR 0.151
// Class Version: 8
package me.recolance.auction.menu.categories;

import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.data.AuctionItem;
import me.recolance.auction.data.DataHandler;
import me.recolance.auction.menu.MenuRenderer;
import me.recolance.auction.menu.categories.AuctionCategory;
import me.recolance.auction.menu.categories.AuctionMenu;
import me.recolance.auction.menu.categories.CategoryItemViewMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CategoryMainMenu
        extends AuctionMenu {
    private List<AuctionItem> categories;
    private int page = 1;
    private boolean mainPage = true;

    public CategoryMainMenu(Player player) {
        super(player, "Auction Categories", 54);
    }

    public void setCategories(List<AuctionItem> categories, boolean main) {
        this.categories = categories;
        this.mainPage = main;
    }

    private List<AuctionItem> getCategories() {
        if (this.categories == null || this.categories.isEmpty()) {
            this.categories = Lists.newLinkedList(DataHandler.getAuctionCategories());
            this.mainPage = true;
        }
        return this.categories;
    }

    @Override
    public void redraw() {
        this.clearItems();
        int PREVIOUS_PAGE_SLOT = this.inventory.getSize() - 6;
        int NEXT_PAGE_SLOT = this.inventory.getSize() - 4;
        int MAIN_AUCTION_SLOT = this.inventory.getSize() - 1;
        int BIN_SLOT = this.inventory.getSize() - 9;
        int REFRESH_SLOT = this.inventory.getSize() - 5;
        int MAIN_CATEGORIES = this.inventory.getSize() - 7;
        List<AuctionItem> categories = this.getCategories();
        int perPage = 45;
        int maxPages = (int)Math.ceil((double)categories.size() / 45.0);
        if (this.page > maxPages) {
            this.page = maxPages;
        }
        if (this.page < 1) {
            this.page = 1;
        }
        if (this.page > 1) {
            int previousPage = Math.max(1, this.page - 1);
            this.setItem(PREVIOUS_PAGE_SLOT, new ItemBuilder(Material.ARROW, CC.RedB + "Previous Page (" + CC.Gray + previousPage + CC.Red + "/" + CC.Gray + maxPages + CC.RedB + ")", new String[]{CC.Gray + "Click to view previous category page."}).build(), () -> {
                if (!this.canClick()) {
                    return;
                }
                --this.page;
                this.nextClickExpiration = System.currentTimeMillis() + 100L;
                this.redraw();
            });
        }
        if (maxPages > 1 && this.page < maxPages) {
            int nextPage = Math.min(maxPages, this.page + 1);
            this.setItem(NEXT_PAGE_SLOT, new ItemBuilder(Material.ARROW, CC.GreenB + "Next Page (" + CC.Gray + nextPage + CC.Green + "/" + CC.Gray + maxPages + CC.GreenB + ")", new String[]{CC.Gray + "Click to view next category page."}).build(), () -> {
                if (!this.canClick()) {
                    return;
                }
                ++this.page;
                this.nextClickExpiration = System.currentTimeMillis() + 100L;
                this.redraw();
            });
        }
        this.setItem(MAIN_AUCTION_SLOT, new ItemBuilder(Material.PAPER, CC.YellowB + "Main Auction Menu", new String[]{CC.Gray + "Click to view the main auction menu."}).build(), () -> this.closeWithDelay(pl -> MenuRenderer.openAuction(this.player)));
        this.setItem(BIN_SLOT, this.getCollectionBinItem());
        this.setItem(REFRESH_SLOT, new ItemBuilder(Material.WATCH, CC.GreenB + "Refresh", new String[]{CC.Gray + "Click to refresh the auction page!"}).build(), () -> {
            if (!this.canClick()) {
                return;
            }
            this.nextClickExpiration = System.currentTimeMillis() + 100L;
            this.redraw();
        });
        if (!this.mainPage) {
            this.setItem(MAIN_CATEGORIES, new ItemBuilder(Material.CHEST, CC.RedB + "Main Categories", new String[]{CC.Gray + "Click to view main category page."}).build(), () -> {
                if (!this.canClick()) {
                    return;
                }
                this.page = 1;
                this.nextClickExpiration = System.currentTimeMillis() + 100L;
                this.categories = Lists.newLinkedList(DataHandler.getAuctionCategories());
                this.mainPage = true;
                this.redraw();
            });
        }
        int startIndex = this.page * 45 - 45;
        int endIndex = startIndex + 45;
        int index = 0;
        for (int i = startIndex; i < endIndex && i < categories.size(); ++i) {
            AuctionItem item = categories.get(i);
            ItemStack display = item.getDisplay();
            ItemMeta im = display.getItemMeta();
            if (item instanceof AuctionCategory) {
                AuctionCategory category = (AuctionCategory)item;
                if (category.getCategoryItems().size() == 1 && category.getSubCategories().isEmpty()) {
                    this.setItem(index++, this.getAuctionItem(category.getCategoryItems().get(0)));
                    continue;
                }
                ArrayList<String> lore = Lists.newArrayList();
                lore.add("");
                if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                    lore.add(CC.Gold + "Sub Categories: " + CC.Yellow + category.getSubCategories().size());
                }
                lore.add(CC.GoldB + "Listings: " + CC.Yellow + category.getCategoryItems().size());
                lore.add(CC.Gold + " Lowest Price: " + CC.YellowB + "$" + CC.Yellow + NumberUtils.formatMoney(category.getLowestPricedItem()));
                if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                    lore.add(CC.Gray + "Click to view sub categories!");
                }
                im.setLore(lore);
                display.setItemMeta(im);
                this.setItem(index++, new InventoryItem(display).click(() -> {
                    if (category.getSubCategories() != null && category.getSubCategories().size() > 0) {
                        this.categories = Lists.newLinkedList(category.getSubCategories());
                        if (category.getCategoryItems() != null && !category.getCategoryItems().isEmpty()) {
                            this.categories.addAll(category.getCategoryItems());
                        }
                        this.mainPage = false;
                        this.redraw();
                        return;
                    }
                    new CategoryItemViewMenu(this.player, category).setParentGUI(this).openGUI(Auction.getPlugin());
                }));
                continue;
            }
            if (!(item instanceof Listing)) continue;
            this.setItem(index++, this.getAuctionItem((Listing)item));
        }
    }

    public void setCategories(List<AuctionItem> categories) {
        this.categories = categories;
    }
}
