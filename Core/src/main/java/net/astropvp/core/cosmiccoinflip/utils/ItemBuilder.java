/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.SkullMeta
 */
package net.astropvp.core.cosmiccoinflip.utils;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    public static ItemStack buildItem(Material m, short data, String name, String ... lore) {
        return ItemBuilder.buildItem(m, 1, data, name, lore);
    }

    public static ItemStack createSkull(String owner, String name, String ... lore) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta)is.getItemMeta();
        sm.setOwner(owner);
        sm.setDisplayName(name);
        sm.setLore(new ArrayList<String>(Arrays.asList(lore)));
        is.setItemMeta(sm);
        return is;
    }

    public static ItemStack buildItem(Material m, short data, String name, List<String> lore) {
        return ItemBuilder.buildItem(m, 1, data, name, lore.toArray(new String[lore.size()]));
    }

    public static ItemStack buildItem(Material m, int amount, short data, String name, String ... lore) {
        ItemStack is = new ItemStack(m, amount, data);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (lore != null) {
            ArrayList<String> lores = new ArrayList<String>();
            for (String s : lore) {
                if (s == null) continue;
                if (s.contains("//")) {
                    lores.addAll(Lists.newArrayList(StringUtils.split(s, "//")));
                    continue;
                }
                lores.add(s);
            }
            im.setLore(lores);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack buildItem(Material m, int amount, short data, String name, List<String> lore) {
        ItemStack is = new ItemStack(m, amount, data);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (lore != null) {
            im.setLore(lore);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack buildItem(Material m, String name, String ... lore) {
        return ItemBuilder.buildItem(m, (short)0, name, lore);
    }
}

