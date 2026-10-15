package org.arkhamnetwork.enchantments.util;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtils {
    public static ItemStack buildItem(final Material m, final short metaData, final String name, final String... description) {
        final ItemStack is = new ItemStack(m, 1, metaData);
        final ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (description != null) {
            im.setLore((List) Arrays.asList(description));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack setTypeAndDurability(final ItemStack i, final Material m, final short dura) {
        i.setType(m);
        i.setDurability(dura);
        return i;
    }

    public static ItemStack injectLore(final ItemStack i, final String... loreLines) {
        final ItemMeta im = i.getItemMeta();
        final List<String> newLore = new ArrayList<String>();
        for (final String s : loreLines) {
            newLore.add(s);
        }
        for (final String s2 : im.getLore()) {
            newLore.add(s2);
        }
        im.setLore((List) newLore);
        i.setItemMeta(im);
        return i;
    }

    public static ItemStack addGlow(final ItemStack item) {
        return new NBTWrapper(item).setGlowing().build();
    }
}
