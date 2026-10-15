// 
// Decompiled by Procyon v0.5.36
// 

package me.ifamasssxd.cosmicconquest.libs;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class ItemBuilder
{
    public static ItemStack buildItem(final Material m, final short data, final String name, final String... lore) {
        return buildItem(m, 1, data, name, lore);
    }
    
    public static ItemStack createSkull(final String owner, final String name, final String... lore) {
        final ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta sm = (SkullMeta)is.getItemMeta();
        sm.setOwner(owner);
        sm.setDisplayName(name);
        sm.setLore((List)new ArrayList(Arrays.asList(lore)));
        is.setItemMeta((ItemMeta)sm);
        return is;
    }
    
    public static ItemStack buildItem(final Material m, final short data, final String name, final List<String> lore) {
        return buildItem(m, 1, data, name, (String[])lore.toArray(new String[lore.size()]));
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
                        lores.addAll(Lists.newArrayList(StringUtils.split(s, "//")));
                    }
                    else {
                        lores.add(s);
                    }
                }
            }
            im.setLore((List)lores);
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
            im.setLore((List)lore);
        }
        is.setItemMeta(im);
        return is;
    }
    
    public static ItemStack buildItem(final Material m, final String name, final String... lore) {
        return buildItem(m, (short)0, name, lore);
    }
}
