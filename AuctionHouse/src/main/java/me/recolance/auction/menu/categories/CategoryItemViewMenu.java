// Decompiled with: CFR 0.151
// Class Version: 8
package me.recolance.auction.menu.categories;

import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import java.util.List;
import me.recolance.auction.Auction;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.menu.categories.AuctionCategory;
import me.recolance.auction.menu.categories.AuctionMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CategoryItemViewMenu
        extends AuctionMenu {
    private AuctionCategory category;
    private int page = 1;

    public CategoryItemViewMenu(Player player, AuctionCategory category) {
        super(player, "Category Items", 54);
        this.category = category;
    }

    @Override
    public void redraw() {
        this.clearItems();
        int PREVIOUS_PAGE_SLOT = this.inventory.getSize() - 6;
        int NEXT_PAGE_SLOT = this.inventory.getSize() - 4;
        int MAIN_CATEGORY_SLOT = this.inventory.getSize() - 1;
        int COLLECTION_BIN_SLOT = this.inventory.getSize() - 9;
        List<Listing> items = this.category.getCategoryItems();
        this.setItem(MAIN_CATEGORY_SLOT, new InventoryItem(new ItemBuilder(Material.PAPER, CC.RedB + "Main Category Menu", new String[]{CC.Gray + "Click to view all current", CC.Gray + "auction item categories."}).build()).click(() -> {
            if (this.getParentGUI() != null) {
                this.getParentGUI().openGUI(Auction.getPlugin());
                this.getParentGUI().redraw();
            }
        }));
        this.setItem(COLLECTION_BIN_SLOT, this.getCollectionBinItem());
        if (items.size() > 0) {
            int perPage = 45;
            int maxPages = (int)Math.ceil((double)items.size() / 45.0);
            if (this.page > maxPages) {
                this.page = maxPages;
            }
            if (this.page < 1) {
                this.page = 1;
            }
            if (this.page > 1) {
                int previousPage = Math.max(1, this.page - 1);
                this.setItem(PREVIOUS_PAGE_SLOT, new InventoryItem(new ItemBuilder(Material.ARROW, CC.RedB + "Previous Page (" + CC.Gray + previousPage + CC.Red + "/" + CC.Gray + maxPages + CC.RedB + ")", new String[]{CC.Gray + "Click to view previous page."}).build()).click(() -> {
                    if (!this.canClick()) {
                        return;
                    }
                    --this.page;
                    this.nextClickExpiration = System.currentTimeMillis() + 100L;
                    this.redraw();
                }));
            }
            if (maxPages > 1 && this.page < maxPages) {
                int nextPage = Math.min(maxPages, this.page + 1);
                this.setItem(NEXT_PAGE_SLOT, new InventoryItem(new ItemBuilder(Material.ARROW, CC.GreenB + "Next Page (" + CC.Gray + nextPage + CC.Green + "/" + CC.Gray + maxPages + CC.GreenB + ")", new String[]{CC.Gray + "Click to view next page."}).build()).click(() -> {
                    if (!this.canClick()) {
                        return;
                    }
                    ++this.page;
                    this.nextClickExpiration = System.currentTimeMillis() + 100L;
                    this.redraw();
                }));
            }
            int startIndex = this.page * 45 - 45;
            int endIndex = startIndex + 45;
            int index = 0;
            for (int i = startIndex; i < endIndex && i < items.size(); ++i) {
                Listing item = items.get(i);
                this.setItem(index++, this.getAuctionItem(item));
            }
        }
    }
}
