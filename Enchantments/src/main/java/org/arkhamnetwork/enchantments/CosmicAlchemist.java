package org.arkhamnetwork.enchantments;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.listeners.EnchanterListener;
import org.arkhamnetwork.enchantments.util.ItemUtils;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CosmicAlchemist {
    public static ItemStack block;
    public static ItemStack pending;
    public static ItemStack preview;
    public static ItemStack confirmed;

    static {
        CosmicAlchemist.block = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 15, ChatColor.BLACK.toString() + ".", new String[0]);
        CosmicAlchemist.pending = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 14, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "THE COSMIC ALCHEMIST", ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "This NPC will Exchange...", "", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "*" + ChatColor.WHITE + " 2X Enchantment Books", ChatColor.GRAY + "(of the same type and level)", ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "=" + ChatColor.WHITE + " 1X Enchantment Book", ChatColor.GRAY + "(of higher level)", "", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "*" + ChatColor.WHITE + " 2X Magic Dust", ChatColor.GRAY + "(of the same rarity)", ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "=" + ChatColor.WHITE + " 1X Magic Dust", ChatColor.GRAY + "(of higher rarity)");
        CosmicAlchemist.preview = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 0, ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "ITEM PREVIEW", ChatColor.GRAY + "A preview of the item you will", ChatColor.GRAY + "recieve from the Cosmic Alchemist", ChatColor.GRAY + "will be displayed here.");
        CosmicAlchemist.confirmed = ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 5, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "THANK YOU!", ChatColor.GRAY + "You may now claim your combined item.");
    }

    public static Inventory getExchangeInventory() {
        final Inventory inv = Bukkit.createInventory((InventoryHolder) null, 27, "Cosmic Alchemist");
        for (int i = 0; i < inv.getSize(); ++i) {
            if (i != 3 && i != 5) {
                if (i != 22) {
                    inv.setItem(i, CosmicAlchemist.block);
                }
            }
        }
        inv.setItem(13, CosmicAlchemist.preview);
        inv.setItem(22, CosmicAlchemist.pending);
        return inv;
    }

    public static ItemStack getAcceptItem(final Player p, final List<ItemStack> offer) {
        return ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short) 0, ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "CLICK TO EXCHANGE", ChatColor.RED.toString() + ChatColor.BOLD.toString() + "COST " + ChatColor.RED + getAlchemistCost(p, offer) + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "XP", "", ChatColor.GRAY + "Click here to confirm this", ChatColor.GRAY + "item exchange and recieve your", ChatColor.GRAY + "epic new item shown above!");
    }

    public static ItemStack getCombinedBook(final List<ItemStack> Books) {
        final ItemStack book1 = Books.get(0);
        final ItemStack book2 = Books.get(1);
        final CustomEnchantment book1Enchant = EnchantmentBook.getEnchantment(book1);
        if (book1Enchant.heroicEnchantment) {
            return null;
        }
        final int bookLevel = EnchantmentBook.getEnchantmentLevel(book1) + 1;
        final int successRate = Math.min(99, (int) (EnchantmentBook.getEnchantmentBookSuccessRate(book1) * 0.25 + EnchantmentBook.getEnchantmentBookSuccessRate(book2) * 0.25));
        final int baseDestroyRate = Math.max(EnchantmentBook.getEnchantmentBookDestructionRate(book1), EnchantmentBook.getEnchantmentBookDestructionRate(book2));
        final int destroyRate = Math.min(100, baseDestroyRate + Math.min(25, (EnchantmentBook.getEnchantmentBookDestructionRate(book1) + EnchantmentBook.getEnchantmentBookDestructionRate(book2)) / 4));
        return EnchantmentBook.getEnchantmentBook(EnchantmentBook.getEnchantment(book1), bookLevel, successRate, destroyRate);
    }

    @Deprecated
    public static ItemStack getCombinedBook_LEGACY(final List<ItemStack> Books) {
        ItemStack baseBook = null;
        int enchLevelBase = 0;
        int successRateBase = 0;
        int destroyRateBase = 0;
        final List<Double> levels = new ArrayList<Double>();
        for (final ItemStack Book : Books) {
            if (baseBook == null) {
                baseBook = Book;
                enchLevelBase = EnchantmentBook.getEnchantmentLevel(Book);
                successRateBase = EnchantmentBook.getEnchantmentBookSuccessRate(Book);
                destroyRateBase = EnchantmentBook.getEnchantmentBookDestructionRate(Book);
                levels.add((double) enchLevelBase);
            } else {
                final int enchLevel = EnchantmentBook.getEnchantmentLevel(Book);
                final int successRate = EnchantmentBook.getEnchantmentBookSuccessRate(Book);
                final int destroyRate = EnchantmentBook.getEnchantmentBookDestructionRate(Book);
                levels.add((double) enchLevel);
                if (enchLevel > enchLevelBase) {
                    baseBook = Book;
                    enchLevelBase = EnchantmentBook.getEnchantmentLevel(Book);
                    successRateBase = EnchantmentBook.getEnchantmentBookSuccessRate(Book);
                    destroyRateBase = EnchantmentBook.getEnchantmentBookDestructionRate(Book);
                } else {
                    if (enchLevel != enchLevelBase) {
                        continue;
                    }
                    if (successRate > successRateBase) {
                        baseBook = Book;
                        enchLevelBase = EnchantmentBook.getEnchantmentLevel(Book);
                        successRateBase = EnchantmentBook.getEnchantmentBookSuccessRate(Book);
                        destroyRateBase = EnchantmentBook.getEnchantmentBookDestructionRate(Book);
                    } else {
                        if (successRate != successRateBase || destroyRate >= destroyRateBase) {
                            continue;
                        }
                        baseBook = Book;
                        enchLevelBase = EnchantmentBook.getEnchantmentLevel(Book);
                        successRateBase = EnchantmentBook.getEnchantmentBookSuccessRate(Book);
                        destroyRateBase = EnchantmentBook.getEnchantmentBookDestructionRate(Book);
                    }
                }
            }
        }
        Books.remove(baseBook);
        double successRateMod = 0.0;
        double destroyRateMod = 0.0;
        final int avgLevel = (int) Math.round((levels.get(0) + levels.get(1)) / levels.size());
        for (final ItemStack Book2 : Books) {
            final double enchLevel2 = EnchantmentBook.getEnchantmentLevel(Book2);
            final double successRate2 = EnchantmentBook.getEnchantmentBookSuccessRate(Book2);
            final double destroyRate2 = EnchantmentBook.getEnchantmentBookDestructionRate(Book2);
            successRateMod += successRate2 * 0.2;
            destroyRateMod += destroyRate2 * 0.1;
        }
        return EnchantmentBook.getEnchantmentBook(EnchantmentBook.getEnchantment(baseBook), enchLevelBase, Math.min(95, successRateBase + (int) Math.round(successRateMod)), Math.max(1, destroyRateBase - (int) Math.round(destroyRateMod)));
    }

    public static ItemStack getCombinedDust(final List<ItemStack> offer) {
        int combinedDustIncrease = 0;
        final ItemRarity oldRarity = getMagicDustRarity(offer.get(0));
        for (final ItemStack i : offer) {
            combinedDustIncrease += EnchantmentBook.getMagicDustSuccessIncrease(i);
        }
        final ItemRarity newRarity = CosmicSpaceChestsAPI.getItemRarity(CosmicSpaceChestsAPI.getItemRarity(oldRarity) + 1);
        final ItemStack newDust = EnchantmentBook.getMagicDust(newRarity, Math.max(1, combinedDustIncrease / 2));
        return newDust;
    }

    public static boolean areBooksSameType(final List<ItemStack> items) {
        if (items.size() == 0) {
            return true;
        }
        final CustomEnchantment previousEnchant = EnchantmentBook.getEnchantment(items.get(0));
        for (final ItemStack i : items) {
            if (!EnchantmentBook.isEnchantmentBook(i) || !EnchantmentBook.getEnchantment(i).equals((Object) previousEnchant)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areBooksSameLevel(final List<ItemStack> items) {
        if (items.size() == 0) {
            return true;
        }
        final int previousLevel = EnchantmentBook.getEnchantmentLevel(items.get(0));
        for (final ItemStack i : items) {
            if (!EnchantmentBook.isEnchantmentBook(i) || EnchantmentBook.getEnchantmentLevel(i) != previousLevel) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDustSameTier(final List<ItemStack> items) {
        if (items.size() == 0) {
            return true;
        }
        final ItemRarity previousTier = getMagicDustRarity(items.get(0));
        for (final ItemStack i : items) {
            if (!EnchantmentBook.isMagicDust(i) || getMagicDustRarity(i) != previousTier) {
                return false;
            }
        }
        return true;
    }

    public static ItemRarity getMagicDustRarity(final ItemStack dust) {
        return ItemRarity.valueOf(ChatColor.stripColor(dust.getItemMeta().getDisplayName().split(" ")[0].toUpperCase()));
    }

    public static int getAlchemistCost(final Player p, final List<ItemStack> items) {
        double cost = 0.0;
        double highestCost = 0.0;
        int totalDustSuccess = 0;
        for (final ItemStack i : items) {
            if (EnchantmentBook.isMagicDust(i)) {
                totalDustSuccess += EnchantmentBook.getMagicDustSuccessIncrease(i);
            }
        }
        final int newDustSuccess = totalDustSuccess / 2;
        for (final ItemStack j : items) {
            if (EnchantmentBook.isEnchantmentBook(j)) {
                final double bookLevel = EnchantmentBook.getEnchantmentLevel(j);
                final double maxLevel = EnchantmentBook.getEnchantment(j).getMaxLevel();
                final double bookPrice = EnchanterListener.getEnchanterBookPrice(CosmicSpaceChestsAPI.getItemRarity(TieredEnchantments.getEnchantmentTier(EnchantmentBook.getEnchantment(j).name())));
                cost = bookPrice * (1.0 + bookLevel / maxLevel * 0.3);
                highestCost = Math.max(highestCost, cost);
            }
            if (EnchantmentBook.isMagicDust(j)) {
                final double dustPower = newDustSuccess * 0.01;
                final double bookPrice2 = EnchanterListener.getEnchanterBookPrice(CosmicSpaceChestsAPI.getItemRarity(CosmicSpaceChestsAPI.getItemRarity(getMagicDustRarity(j)) + 1));
                cost = dustPower * 25.0 * bookPrice2;
                highestCost = Math.max(highestCost, cost);
            }
        }

        return (int) (highestCost * 0.65);
    }
}
