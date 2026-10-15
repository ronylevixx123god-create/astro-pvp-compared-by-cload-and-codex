/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package net.astropvp.core.utils;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    public static ItemStack buildItem(Material m, short metaData, String name) {
        ItemStack is = new ItemStack(m, 1, metaData);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack buildItem(Material m, short metaData, String name, String... description) {
        ItemStack is = new ItemStack(m, 1, metaData);
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

    public static ItemStack buildItem(Material m, short metaData, String name, LinkedList<String> description) {
        ItemStack is = new ItemStack(m, 1, metaData);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (description != null) {
            im.setLore(description);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack setAmount(ItemStack i, int x) {
        i.setAmount(x);
        return i;
    }

    public static ItemStack addGlow(ItemStack item) {
        return new NBTWrapper(item).setGlowing().build();
    }

    public static void giveItemToPlayer(Player p, ItemStack i) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(new ItemStack[]{i});
        } else {
            Bukkit.getLogger().info("[CosmicDungeons] " + p.getName() + " did not have inventory space for " + (Object) i + ", dropping on ground!");
            p.getWorld().dropItem(p.getLocation(), i);
        }
    }


    public static ItemStack getNextPageItem(int page, String name) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(CC.GreenB) + "Next Page");
        meta.setLore(Arrays.asList(new String[]{String.valueOf(CC.Gray) + "Click to view the next page."}));
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        wrapper.setInteger("page", Integer.valueOf(page));
        wrapper.setString("player", name);
        item = wrapper.build();
        return item;
    }

    public static ItemStack getPreviousPageItem(int page, String name) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(CC.RedB) + "Previous Page");
        meta.setLore(Arrays.asList(new String[]{String.valueOf(CC.Gray) + "Click to view the last page."}));
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        wrapper.setInteger("page", Integer.valueOf(page));
        wrapper.setString("player", name);
        item = wrapper.build();
        return item;
    }

    public static ItemStack getRevivePlayerItem(int page, String name) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.valueOf(CC.GreenB) + "Revive Player");
        meta.setLore(Arrays.asList(new String[]{String.valueOf(CC.Gray) + "Click to revive " + name + "."}));
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        wrapper.setInteger("page", Integer.valueOf(page));
        wrapper.setString("player", name);
        item = wrapper.build();
        return item;
    }
}



