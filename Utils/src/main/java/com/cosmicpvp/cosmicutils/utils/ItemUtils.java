/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.CommandSource
 *  com.earth2me.essentials.Essentials
 *  com.earth2me.essentials.MetaItemStack
 *  com.earth2me.essentials.textreader.SimpleTextInput
 *  net.ess3.api.IEssentials
 *  org.bukkit.craftbukkit.v1_7_R4.me.astropvp.cratekeys.inventory.CraftItemStack
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.google.common.collect.Lists;
import net.ess3.api.IEssentials;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemUtils {
    private static String capTitle = Pattern.quote("{TITLE}:");
    private static String lowerTitle = Pattern.quote("{title}:");

    public static String getDisplayName(ItemStack is) {
        return is.hasItemMeta() && is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : StringUtils.capitaliseAllWords(is.getType().name().toLowerCase().replace("_", " "));
    }

    public static int getMaxDurability(ItemStack item) {
        if (item == null) {
            return 0;
        }
        Material type = item.getType();
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder builder = wrapper.getCompound("conaxData");
        if (builder != null && builder.hasKey("durability") && builder.hasKey("maxDurability")) {
            return builder.getInt("maxDurability");
        }
        return type.getMaxDurability();
    }

    public static int getCurrentDurability(ItemStack item) {
        if (item == null) {
            return 0;
        }
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder builder = wrapper.getCompound("conaxData");
        if (builder != null && builder.hasKey("durability") && builder.hasKey("maxDurability")) {
            return builder.getInt("durability");
        }
        return item.getDurability();
    }

    public static boolean areItemsSimilarIgnoringIID(ItemStack item, ItemStack itemWithIID) {
        ItemMeta itemWithIIDMeta;
        ItemMeta itemMeta;
        if (item == null || itemWithIID == null) {
            return false;
        }
        return item.getType() == itemWithIID.getType() && item.getDurability() == itemWithIID.getDurability() && item.hasItemMeta() && itemWithIID.hasItemMeta() && (itemMeta = item.getItemMeta()).hasDisplayName() && (itemWithIIDMeta = itemWithIID.getItemMeta()).hasDisplayName() && itemMeta.getDisplayName().equals(itemWithIIDMeta.getDisplayName()) && itemMeta.hasLore() && itemWithIIDMeta.hasLore() && ItemUtils.isLoreSameWithoutIID(itemMeta.getLore(), itemWithIIDMeta.getLore());
    }

    public static boolean isLoreSameWithoutIID(List<String> lore, List<String> otherLore) {
        if (lore == null || otherLore == null) {
            return false;
        }
        if (lore.size() > otherLore.size()) {
            return false;
        }
        for (int i = 0; i < lore.size(); ++i) {
            String line = lore.get(i);
            String otherLine = otherLore.get(i);
            if (otherLine.contains("IID") || line.equals(otherLine)) continue;
            return false;
        }
        return true;
    }

    public static ItemStack decreaseItemStackAmountByOne(ItemStack itemStack) {
        if (itemStack.getAmount() == 1) return null;

        itemStack.setAmount(itemStack.getAmount() - 1);
        return itemStack;
    }

    public static MaterialData parseData(String str) {
        Material mat;
        short data = 0;
        try {
            if (str.contains(":")) {
                data = Short.parseShort(str.split(":")[1]);
                String first = str.split(":")[0];
                Integer id = NumberUtils.parseIntOrNull(first);
                mat = id != null ? Material.getMaterial(id) : Material.getMaterial(first);
            } else {
                Integer id = NumberUtils.parseIntOrNull(str);
                mat = id != null ? Material.getMaterial(id) : Material.getMaterial(str);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid Material Data given: " + str);
            e.printStackTrace();
            return null;
        }
        return new MaterialData(mat, (byte) data);
    }

    public static List<ItemStack> parseItemsIntoItemStacks(List<String> items) {
        if (!Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            return null;
        }
        Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        ArrayList<ItemStack> retr = Lists.newArrayList();
        try {
            SimpleTextInput e = new SimpleTextInput(items);
            boolean allowUnsafe = ess.getSettings().allowUnsafeEnchantments();
            for (String kitItem : e.getLines()) {
                String titleData;
                boolean containsLower = kitItem.contains(lowerTitle);
                if ((containsLower || kitItem.contains(capTitle)) && !(titleData = kitItem.split(containsLower ? lowerTitle : capTitle)[1]).isEmpty()) {
                    retr.add(CustomItemUtils.getTitleVoucher(ChatColor.translateAlternateColorCodes('&', titleData)));
                    continue;
                }
                String[] parts = kitItem.split(" +");
                ItemStack parseStack = ess.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack.getType() == Material.AIR) continue;
                MetaItemStack metaStack = new MetaItemStack(parseStack);
                if (parts.length > 2) {
                    metaStack.parseStringMeta((CommandSource) null, allowUnsafe, parts, 2, (IEssentials) ess);
                }
                retr.add(metaStack.getItemStack());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retr;
    }

    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }

    public static ItemStack createEnchantBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) book.getItemMeta();
        storageMeta.getStoredEnchants().keySet().forEach(ench -> storageMeta.removeEnchant(enchantment));
        storageMeta.addStoredEnchant(enchantment, level, true);
        book.setItemMeta(storageMeta);
        return book;
    }
}

