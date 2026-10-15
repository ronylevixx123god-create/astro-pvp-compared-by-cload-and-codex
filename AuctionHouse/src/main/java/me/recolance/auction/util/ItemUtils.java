/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.me.astropvp.cratekeys.inventory.CraftItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.recolance.auction.util;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemUtils {
    public static org.bukkit.inventory.ItemStack buildItem(Material m, short metaData, String name, String... description) {
        org.bukkit.inventory.ItemStack is = new org.bukkit.inventory.ItemStack(m, 1, metaData);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (description != null) {
            im.setLore(Arrays.asList(description));
        }
        is.setItemMeta(im);
        return is;
    }

    public static org.bukkit.inventory.ItemStack setNBTString(org.bukkit.inventory.ItemStack item, String key, String data) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item);
        NBTTagCompound compound = nmsItem.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        compound.setString(key, data);
        nmsItem.setTag(compound);
        return CraftItemStack.asCraftMirror((ItemStack) nmsItem);
    }

    public static org.bukkit.inventory.ItemStack setFakeEnchantment(org.bukkit.inventory.ItemStack item, boolean enchant) {
        if (!enchant) {
            return item;
        }
        ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item);
        NBTTagCompound compound = nmsItem.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        compound.set("ench", (NBTBase) new NBTTagCompound());
        nmsItem.setTag(compound);
        return CraftItemStack.asCraftMirror((ItemStack) nmsItem);
    }

    public static String getNBTString(org.bukkit.inventory.ItemStack item, String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item);
        NBTTagCompound compound = nmsItem.getTag();
        if (compound == null) {
            return null;
        }
        if (!compound.hasKey(key)) {
            return null;
        }
        return compound.getString(key);
    }
}

