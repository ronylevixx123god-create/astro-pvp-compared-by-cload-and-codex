
package me.ifamasssxd.cosmicduel.utils;

import com.google.common.collect.Lists;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class KitUtils {
    public static String ITEM_WATERMARK = (Object)ChatColor.GOLD + ChatColor.BOLD.toString() + "DUEL KIT ITEM";
    public static String ENVOY_WATERMARK = (Object)ChatColor.GOLD + ChatColor.BOLD.toString() + "DUEL ENVOY ITEM";
    private static List<ItemStack> edittableItems = Lists.newArrayList(new ItemStack(Material.POTION, 1, (short) 16421), new ItemStack(Material.POTION, 1, (short) 8226), new ItemStack(Material.POTION, 1, (short) 16418), new ItemStack(Material.POTION, 1, (short) 8233), new ItemStack(Material.POTION, 1, (short) 16425), new ItemStack(Material.POTION, 1, (short) 8259), new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.COOKED_BEEF, 64), new ItemStack(Material.GOLDEN_CARROT, 64));
    private static List<Material> armor = Lists.newArrayList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);



    public static ItemStack addDuelWatermark(ItemStack item) {
        List<String> lore;
        ItemMeta im = (item = item.clone()).getItemMeta();
        List list = lore = im.hasLore() ? im.getLore() : Lists.newArrayList();
        if (lore.size() > 0 && ((String)lore.get(lore.size() - 1)).equals(ITEM_WATERMARK)) {
            return item;
        }
        lore.add(ITEM_WATERMARK);
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public static void addEnvoyWatermark(ItemStack item) {
        List<String> lore;
        ItemMeta im = (item = item.clone()).getItemMeta();
        List list = lore = im.hasLore() ? im.getLore() : Lists.newArrayList();
        if (lore.size() > 0 && lore.contains(ENVOY_WATERMARK)) {
            return;
        }
        lore.add("");
        lore.add(ENVOY_WATERMARK);
        im.setLore(lore);
        item.setItemMeta(im);
    }







    private static boolean doesInventoryContain(ItemStack item, Inventory inventory) {
        for (ItemStack is : inventory) {
            if (is == null || is.getType() == Material.AIR || item.getType() != is.getType() || item.getDurability() != is.getDurability()) continue;
            return true;
        }
        return false;
    }

    private static boolean canEditKitItem(ItemStack item) {
        return !DuelUtils.isWeapon(item) && !DuelUtils.isArmor(item) && item.getType() != Material.GOLDEN_APPLE && item.getType() != Material.ARROW;
    }



    private static boolean containsType(List<ItemStack> items, Material material) {
        for (ItemStack gkitItem : items) {
            if (gkitItem.getType() != material) continue;
            return true;
        }
        return false;
    }
}

