// Decompiled with: FernFlower
// Class Version: 8
package me.recolance.auction.menu.categories;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import me.recolance.auction.auction.Listing;
import me.recolance.auction.data.AuctionItem;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AuctionCategory implements Comparable<AuctionCategory>, AuctionItem {
    private String displayName;
    private String strippedName;
    private ItemStack displayItem;
    private List<Listing> categoryItems = Lists.newLinkedList();
    private Listing lowestPricedItem;
    private List<AuctionCategory> subCategories;
    private Integer rarityTier = 0;

    public double getLowestPricedItem() {
        if (this.lowestPricedItem == null || this.lowestPricedItem.isExpired() || !this.lowestPricedItem.isValid()) {
            this.lowestPricedItem = null;
            this.lowestPricedItem = (Listing)this.categoryItems.stream().min(Comparator.comparing(Listing::getPrice)).orElse(null);
        }

        return this.lowestPricedItem != null ? this.lowestPricedItem.getPrice() : 0.0D;
    }

    public List<Listing> getCategoryItems() {
        this.categoryItems.removeIf((item) -> {
            return !item.isValid() || item.isExpired();
        });
        return this.categoryItems;
    }

    public void addSubCategoryItem(AuctionCategory subCategory) {
        this.subCategories.add(subCategory);
    }

    public void sortSubCategories() {
        this.subCategories.sort(Comparator.comparingInt(AuctionCategory::getRarityTier));
    }

    public void sortCategoryItems() {
        if (this.displayItem != null && this.displayItem.getType() == Material.EXP_BOTTLE && ArkPlugins.EXP_BOTTLES.getArkPlugin().isRunning() && ExpBottles.isXpBottle(this.displayItem)) {
            int exp = ExpBottles.getXpAmount(this.displayItem);
            if (exp > 0) {
                this.getCategoryItems().sort(Comparator.comparingDouble(Listing::getExpPerDollar));
            }

        } else {
            this.getCategoryItems().sort((item1, item2) -> {
                return item1.getPricePerItem() == item2.getPricePerItem() ? item1.compareTo(item2) : Double.compare(item1.getPricePerItem(), item2.getPricePerItem());
            });
        }
    }

    public int compareTo(AuctionCategory o) {
        return this.getDisplayItem().getType().compareTo(o.getDisplayItem().getType());
    }

    public ItemStack getDisplay() {
        return this.getDisplayItem();
    }

    public AuctionCategory(String displayName, String strippedName, ItemStack displayItem, List<Listing> categoryItems, Listing lowestPricedItem, List<AuctionCategory> subCategories, Integer rarityTier) {
        this.displayName = displayName;
        this.strippedName = strippedName;
        this.displayItem = displayItem;
        this.categoryItems = categoryItems;
        this.lowestPricedItem = lowestPricedItem;
        this.subCategories = subCategories;
        this.rarityTier = rarityTier;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getStrippedName() {
        return this.strippedName;
    }

    public ItemStack getDisplayItem() {
        return this.displayItem;
    }

    public List<AuctionCategory> getSubCategories() {
        return this.subCategories;
    }

    public Integer getRarityTier() {
        return this.rarityTier;
    }
}
