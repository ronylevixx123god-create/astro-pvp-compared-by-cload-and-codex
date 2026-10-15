/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.struct;

import me.ifamasssxd.cosmicduel.utils.CustomItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum Kit {
    BYOB("None", Material.DIAMOND_HELMET, 0, null, new ItemStack[0]),
    //SOUP("Soup", Material.MUSHROOM_SOUP, 0, null, new ItemStack[]{new CustomItem(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 5).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 5).addEnchantment(Enchantment.ARROW_INFINITE, 1).buildItem(), new ItemStack(Material.ARROW, 1), new ItemStack(Material.POTION, 3, (short) 8226), new ItemStack(Material.POTION, 3, (short) 8233), new ItemStack(Material.MUSHROOM_SOUP, 24)}),
    //DEBUFF("Potion", Material.SPIDER_EYE, 0, null, new ItemStack[]{new CustomItem(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 5).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new ItemStack(Material.GOLDEN_APPLE, 32), new ItemStack(Material.GOLDEN_CARROT, 64), new CustomItem(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 5).addEnchantment(Enchantment.ARROW_INFINITE, 1).buildItem(), new ItemStack(Material.ARROW, 1), new ItemStack(Material.POTION, 1, (short) 8225), new ItemStack(Material.POTION, 3, (short) 8233), new ItemStack(Material.POTION, 3, (short) 8226), new ItemStack(Material.POTION, 3, (short) 16388), new ItemStack(Material.POTION, 3, (short) 16394), new ItemStack(Material.POTION, 18, (short) 16421)}),
    //HEALER("Potion", Material.BREWING_STAND_ITEM, 0, null, new ItemStack[]{new CustomItem(Material.DIAMOND_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 5).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new CustomItem(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).buildItem(), new ItemStack(Material.GOLDEN_APPLE, 32), new ItemStack(Material.GOLDEN_CARROT, 64), new CustomItem(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 5).addEnchantment(Enchantment.ARROW_INFINITE, 1).buildItem(), new ItemStack(Material.ARROW, 1), new ItemStack(Material.POTION, 1, (short) 8225), new ItemStack(Material.POTION, 3, (short) 8233), new ItemStack(Material.POTION, 3, (short) 8226), new ItemStack(Material.POTION, 20, (short) 16421)}),
    //GKIT("Global Kit", Material.DIAMOND, 0, null, new ItemStack[0]),
    ENVOY("Envoy", Material.CHEST, 0, null, new ItemStack[0]);
    //GODSET("Godset", Material.DIAMOND_CHESTPLATE, 0, null, new ItemStack[0]);

    String name;
    Material material;
    short data;
    String permission;
    List<ItemStack> items = new ArrayList<ItemStack>();

    private Kit(String name, Material material, int data, String perm, ItemStack[] items) {
        this.name = name;
        this.material = material;
        this.data = (short) data;
        this.permission = perm;
        for (ItemStack item : items) {
            this.items.add(item);
        }
    }

    public static Kit getByName(String name, boolean checkBuffType) {
        for (Kit kit : Kit.values()) {
            if (checkBuffType && name.equals(kit.getName(true))) {
                return kit;
            }
            if (!kit.getName().equals(name)) continue;
            return kit;
        }
        return null;
    }

    public boolean hasPermission() {
        return this.permission != null;
    }

    public String getName(boolean includeBuffType) {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public short getData() {
        return this.data;
    }

    public String getPermission() {
        return this.permission;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }
}

