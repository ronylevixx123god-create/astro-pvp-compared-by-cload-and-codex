package me.ifamasssxd.cosmicbosses.utils;


import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    public static ItemStack buildItem(final Material m, final short data, final String name, final String... lore) {
        return buildItem(m, 1, data, name, lore);
    }

    public static ItemStack createSkull(final String owner, final String name, final String... lore) {
        final ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        final SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setOwner(owner);
        sm.setDisplayName(name);
        sm.setLore((List) new ArrayList(Arrays.asList(lore)));
        is.setItemMeta((ItemMeta) sm);
        return is;
    }

    public static ItemStack buildItem(final Material m, final short data, final String name, final List<String> lore) {
        return buildItem(m, 1, data, name, (String[]) lore.toArray(new String[lore.size()]));
    }

    public static ItemStack buildItem(final Material m, final int amount, final short data, final String name, final String... lore) {
        final ItemStack is = new ItemStack(m, amount, data);
        final ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (lore != null) {
            final List<String> lores = new ArrayList<String>();
            for (final String s : lore) {
                if (s != null) {
                    if (s.contains("//")) {
                        lores.addAll(getLore(s));
                    } else {
                        lores.add(s);
                    }
                }
            }
            im.setLore((List) lores);
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack buildItem(final Material m, final int amount, final short data, final String name, final List<String> lore) {
        final ItemStack is = new ItemStack(m, amount, data);
        final ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (lore != null) {
            im.setLore((List) lore);
        }
        is.setItemMeta(im);
        return is;
    }

    private static List<String> getLore(final String s) {
        final List<String> lore = new ArrayList<String>();
        final String color = ChatColor.translateAlternateColorCodes('§', s.trim().substring(0, 2));
        for (final String st : StringUtils.split(s, "//")) {
            lore.add(color + st);
        }
        return lore;
    }

    public static ItemStack buildItem(final Material m, final String name, final String... lore) {
        return buildItem(m, (short) 0, name, lore);
    }

    public static ItemStack buildArmor(final Material material, final Enchantment... enchants) {
        final ItemStack is = new ItemStack(material, 1);
        for (final Enchantment enchant : enchants) {
            is.addUnsafeEnchantment(enchant, enchant.getMaxLevel());
        }
        return is;
    }
}
