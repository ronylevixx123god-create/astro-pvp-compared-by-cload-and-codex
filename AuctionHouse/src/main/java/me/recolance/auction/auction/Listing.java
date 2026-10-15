/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.rit.sucy.CustomEnchantment
 *  org.arkhamnetwork.Arkkit.patches.ExpBottles
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.arkhamnetwork.enchantments.util.TieredEnchantments
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.recolance.auction.auction;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.rit.sucy.CustomEnchantment;
import me.recolance.auction.data.AuctionItem;
import me.recolance.auction.util.Item;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.UUID;

public class Listing
        implements Comparable<Listing>,
        AuctionItem {
    private UUID id;
    private long endTime;
    private UUID listedBy;
    private String listedByName;
    private ItemStack item;
    private double price;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    private boolean valid = true;
    private boolean checkedBook;
    private Integer bookRarity;
    private ChatColor bookTierColor;
    private String bookTierName;
    private Integer expInBottle;
    private Double expPerDollar;
    private double pricePerItem = 0.0;

    public Listing(UUID id, long endTime, UUID listedBy, ItemStack item, double price) {
        this.id = id;
        this.endTime = endTime;
        this.listedBy = listedBy;
        this.item = item;
        this.price = price;
        this.listedByName = Bukkit.getOfflinePlayer((UUID) this.listedBy).getName();
    }

    public double getPricePerItem() {
        if (this.pricePerItem > 0.0) {
            return this.pricePerItem;
        }
        if (this.item.getAmount() > 1) {
            this.pricePerItem = (double) this.getPrice() / (double) this.item.getAmount();
            return this.pricePerItem;
        }
        this.pricePerItem = this.getPrice();
        return this.pricePerItem;
    }

    public int getExpInBottle() {
        if (this.expInBottle == null) {
            this.expInBottle = ExpBottles.isXpBottle((ItemStack) this.item) ? Integer.valueOf(ExpBottles.getXpAmount((ItemStack) this.item)) : Integer.valueOf(0);
        }
        return this.expInBottle;
    }

    public double getExpPerDollar() {
        if (this.expPerDollar == null) {
            int exp = this.getExpInBottle();
            this.expPerDollar = exp > 0 ? Double.valueOf((double) this.price / (double) exp) : Double.valueOf(0.0);
        }
        return this.expPerDollar;
    }

    public Integer getBookRarity() {
        if (!this.checkedBook) {
            this.checkedBook = true;
            if (!EnchantmentBook.isEnchantmentBook((ItemStack) this.item)) {
                return null;
            }
            CustomEnchantment enchant = EnchantmentBook.getEnchantment((ItemStack) this.item);
            if (enchant == null) {
                return null;
            }
            this.bookTierColor = TieredEnchantments.getTierColor((CustomEnchantment) enchant);
            this.bookRarity = TieredEnchantments.getEnchantmentTier((String) enchant.name());
            ItemRarity rarity = CosmicSpaceChestsAPI.getItemRarity((int) this.bookRarity);
            if (rarity == null || rarity == ItemRarity.UNKNOWN) {
                if (this.bookRarity == 7) {
                    this.bookTierName = "Heroic";
                } else if (this.bookRarity == 8) {
                    this.bookTierName = "Mastery";
                }
            } else {
                this.bookTierName = rarity.name;
            }
        }
        return this.bookRarity;
    }

    public ItemStack getMenuDisplayItem() {
        String perData = this.getExpInBottle() > 0 ? CC.Gray + " ($" + NumberUtils.formatMoney((double) this.getExpPerDollar()) + " / xp)" : "";
        double perItem = this.getPricePerItem();
        if (perItem != (double) this.getPrice() && perItem > 0.0) {
            perData = " ($" + NumberUtils.formatMoney((double) perItem) + " / item)";
        }
        return new Item(new ItemStack(this.getItem())).addLore("", "&7-------------------------", "&e&lClick item to buy!", "", "&6Price: &e$" + this.getPrettyPrice() + perData, "&6Seller: &e" + this.getNameOfWhoListed(), "&7-------------------------").get();
    }

    public boolean isExpired() {
        return this.endTime <= System.currentTimeMillis();
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public UUID getWhoListed() {
        return this.listedBy;
    }

    public void setWhoListed(UUID listedBy) {
        this.listedBy = listedBy;
    }

    public String getNameOfWhoListed() {
        return this.listedByName;
    }

    public void setWhoNameOfWhoListed(String name) {
        this.listedByName = name;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPrettyPrice() {
        return this.formatter.format(this.price);
    }

    @Override
    public int compareTo(Listing o) {
        return Long.compare(o.getEndTime(), this.getEndTime());
    }

    @Override
    public ItemStack getDisplay() {
        return this.getMenuDisplayItem();
    }

    public ChatColor getBookTierColor() {
        return this.bookTierColor;
    }

    public String getBookTierName() {
        return this.bookTierName;
    }
}

