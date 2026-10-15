package me.venir.astrocollectors.utils.itemutils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    public static ItemStack buildItem(Material m, short data, String name, String... lore) {
        return buildItem(m, 1, data, name, lore);
    }

    public static ItemStack buildItem(Material m, short data, String name, List<String> lore) {
        return buildItem(m, 1, data, name, lore.<String>toArray(new String[lore.size()]));
    }

    public static ItemStack buildItem(Material m, int amount, short data, String name, String... lore) {
        ItemStack is = new ItemStack(m, amount, data);
        ItemMeta im = is.getItemMeta();
        if (name != null)
            im.setDisplayName(name);
        if (lore != null) {
            List<String> lores = new ArrayList<>();
            byte b = 0;
            String[] arrayOfString;
            for (int i = (arrayOfString = lore).length; b < i; ) {
                String s = arrayOfString[b];
                if (s != null)
                    if (s.contains("//")) {
                        lores.addAll(getLore(s));
                    } else {
                        lores.add(s);
                    }
                b = (byte)(b + 1);
            }
            im.setLore(lores);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack buildItem(Material m, int amount, short data, String name, List<String> lore) {
        ItemStack is = new ItemStack(m, amount, data);
        ItemMeta im = is.getItemMeta();
        if (name != null)
            im.setDisplayName(name);
        if (lore != null)
            im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    private static List<String> getLore(String s) {
        List<String> lore = new ArrayList<>();
        String color = ChatColor.translateAlternateColorCodes('&', s.trim().substring(0, 2));
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = StringUtils.split(s, "//")).length, b = 0; b < i; ) {
            String st = arrayOfString[b];
            lore.add(String.valueOf(color) + st);
            b = (byte)(b + 1);
        }
        return lore;
    }

    public static ItemStack buildItem(Material m, String name, String... lore) {
        return buildItem(m, (short)0, name, lore);
    }
}