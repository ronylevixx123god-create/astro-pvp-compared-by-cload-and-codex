package net.astropvp.core.items.coder;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.rit.sucy.CustomEnchantment;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.astropvp.core.AstroCore;
import org.apache.commons.lang3.StringUtils;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentCoderInventory {
    private static EnchantmentCoderInventory INSTANCE;

    public static EnchantmentCoderInventory getInstance() {
        if (INSTANCE == null)
            INSTANCE = new EnchantmentCoderInventory();
        return INSTANCE;
    }

    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, CC.translate("&8Heroic Enchantment Coder"));
        inventory.setItem(13, getDisplayItem(Material.LEATHER_HELMET));
        inventory.setItem(22, getDisplayItem(Material.LEATHER_CHESTPLATE));
        inventory.setItem(31, getDisplayItem(Material.LEATHER_LEGGINGS));
        inventory.setItem(40, getDisplayItem(Material.LEATHER_BOOTS));
        inventory.setItem(20, getDisplayItem(Material.GOLD_AXE));
        inventory.setItem(24, getDisplayItem(Material.GOLD_SWORD));
        ItemStack pane = getItemPane();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR)
                inventory.setItem(i, pane);
        }
        return inventory;
    }

    public Inventory getChildInventory(UUID uuid, Material type) {
        Inventory inventory = Bukkit.createInventory(null, 36, CC.translate("&8Heroic Enchantment Coder - " + type.toString()));
        inventory.setItem(inventory.getSize() - 9, getBackArrow());
        inventory.setItem(inventory.getSize() - 5, getInfoItem());
        List<CustomEnchantment> enchantments = AstroCore.getInstance().getEnchantmentCoderManager().getEnchantmentsForUser(uuid, type);
        ItemStack pane = getItemPane();
        int i;
        for (i = 0; i < enchantments.size(); i++) {
            CustomEnchantment ench = enchantments.get(i);
            inventory.setItem(i, EnchantmentBook.getEnchantmentBook(ench, ench.getMaxLevel(), 100, 0));
        }
        for (i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR)
                inventory.setItem(i, pane);
        }
        return inventory;
    }

    public void refreshChildInventory(UUID uuid, Material type, Inventory inventory) {
        Inventory childInventory = getChildInventory(uuid, type);
        for (int i = 0; i < childInventory.getSize(); i++) {
            ItemStack item = childInventory.getItem(i);
            if (item != inventory.getItem(i))
                inventory.setItem(i, childInventory.getItem(i));
        }
    }

    private ItemStack getItemPane() {
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(CC.translate("&7"));
        pane.setItemMeta(paneMeta);
        return pane;
    }

    private ItemStack getBackArrow() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&c<- Previous Page"));
        meta.setLore(Collections.singletonList(CC.translate("&7Right-Click to go to the previous page.")));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getInfoItem() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&a&lInformation"));
        meta.setLore(Arrays.asList(CC.translate("&eLeft-Click&7 a book in your inventory to add it to this inventory."),
                CC.translate("&eLeft-Click&7 a book in the item's inventory to return it to your inventory.")));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getDisplayItem(Material material) {
        String name = material.name().split("_")[1];
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&a&l" + StringUtils.capitalize(name.toLowerCase())));
        meta.setLore(Collections.singletonList(CC.translate("&eLeft-Click &7to edit &f&n" + name + "&7 enchantments.")));
        item.setItemMeta(meta);
        return item;
    }
}

