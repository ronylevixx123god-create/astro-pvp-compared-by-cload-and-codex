/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmicguishop.utils;

import com.earth2me.essentials.Essentials;
import me.ifamasssxd.cosmicguishop.struct.ShopMaterialData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class ItemUtils {
    public static String getPrettyName(ShopMaterialData data) {
        return ItemNames.lookup(data);
    }

    public static boolean hasEssentialsPrice(ItemStack item) {
        try {
            Essentials ess = (Essentials) JavaPlugin.getPlugin(Essentials.class);
            if (ess != null && ess.getWorth().getPrice(ess,item) != null) {
                return true;
            }
        } catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    public static ItemStack buildItem(Material m, short metaData, String name, String ... description) {
        ItemStack is = new ItemStack(m, 1, metaData);
        ItemMeta im = is.getItemMeta();
        if (name != null) {
            im.setDisplayName(name);
        }
        if (description != null) {
            im.setLore(Arrays.asList(description));
        }
        is.setItemMeta(im);
        is.setDurability(metaData);
        return is;
    }
}

