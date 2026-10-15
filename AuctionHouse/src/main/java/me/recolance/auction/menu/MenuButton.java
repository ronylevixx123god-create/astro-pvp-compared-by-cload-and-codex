/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.me.astropvp.cratekeys.inventory.CraftItemStack
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.SkullMeta
 */
package me.recolance.auction.menu;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuButton {
    private static List<Integer> attributeItems;

    static {
        int[] idWithAttribute;
        attributeItems = new ArrayList<Integer>();
        for (int item : idWithAttribute = new int[]{256, 257, 258, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 283, 284, 285, 286, 373, 387, 403, 2256, 2257, 2258, 2259, 2260, 2261, 2262, 2263, 2264, 2265, 2266, 2267}) {
            attributeItems.add(item);
        }
    }

    private ItemStack item;

    public MenuButton() {
        this.item = new ItemStack(Material.AIR, 1);
    }

    public MenuButton(ItemStack item) {
        this.item = item;
    }

    private static boolean typeHasAttribute(int id) {
        return attributeItems.contains(id);
    }

    public MenuButton name(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char) '&', (String) name));
        this.item.setItemMeta(meta);
        return this;
    }

    public MenuButton lore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> loreList = new ArrayList<String>();
        if (meta.hasLore()) {
            loreList.addAll(meta.getLore());
        }
        for (String line : lore) {
            loreList.add(ChatColor.translateAlternateColorCodes((char) '&', (String) line));
        }
        meta.setLore(loreList);
        this.item.setItemMeta(meta);
        return this;
    }

    public MenuButton lore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> loreList = new ArrayList<String>();
        if (meta.hasLore()) {
            loreList.addAll(meta.getLore());
        }
        for (String line : lore) {
            loreList.add(ChatColor.translateAlternateColorCodes((char) '&', (String) line));
        }
        meta.setLore(loreList);
        this.item.setItemMeta(meta);
        return this;
    }

    public MenuButton lore(String loreLine) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> loreList = new ArrayList<String>();
        if (meta.hasLore()) {
            loreList.addAll(meta.getLore());
        }
        loreList.add(ChatColor.translateAlternateColorCodes((char) '&', (String) loreLine));
        meta.setLore(loreList);
        this.item.setItemMeta(meta);
        return this;
    }

    public MenuButton skull(UUID uuid) {
        return this.skull(Bukkit.getOfflinePlayer((UUID) uuid).getName());
    }

    public MenuButton skull(String name) {
        SkullMeta meta = (SkullMeta) this.item.getItemMeta();
        meta.setOwner(name);
        this.item.setItemMeta((ItemMeta) meta);
        return this;
    }

    public MenuButton amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public MenuButton type(Material material) {
        return this.type(material.getId());
    }

    public MenuButton type(int typeId) {
        this.item.setTypeId(typeId);
        return this;
    }

    public MenuButton durability(int durability) {
        this.item.setDurability((short) durability);
        return this;
    }

    public ItemStack get() {
        if (MenuButton.typeHasAttribute(this.item.getTypeId())) {
            net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy((ItemStack) this.item);
            NBTTagCompound compound = stack.getTag();
            if (compound == null) {
                compound = new NBTTagCompound();
                stack.setTag(compound);
                compound = stack.getTag();
            }
            compound.setInt("HideFlags", 63);
            stack.setTag(compound);
            this.item = CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) stack);
        }
        return this.item;
    }

    public MenuButton addGlow() {
        this.item.addUnsafeEnchantment(Enchantment.DURABILITY, -1);
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy((ItemStack) this.item);
        NBTTagCompound compound = stack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTag(compound);
            compound = stack.getTag();
        }
        if (!compound.hasKey("HideFlags")) {
            compound.setInt("HideFlags", 1);
        }
        stack.setTag(compound);
        this.item = CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) stack);
        return this;
    }
}

