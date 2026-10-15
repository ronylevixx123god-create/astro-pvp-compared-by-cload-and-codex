/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.utils.NBTWrapper
 *  org.bukkit.inventory.ItemStack
 */
package net.astropvp.lootboxes.util;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NBTUtil {
    public static ItemStack setBoolean(ItemStack item, String key, boolean bool) {
        return new NBTWrapper(item).setBoolean(key, Boolean.valueOf(bool)).build();
    }

    public static boolean getBoolean(ItemStack item, String key) {
        return new NBTWrapper(item).getBoolean(key);
    }

    public static ItemStack setString(ItemStack item, String key, String value) {
        return new NBTWrapper(item).setString(key, value).build();
    }

    public static String getString(ItemStack item, String key) {
        return new NBTWrapper(item).getString(key);
    }

    public static ItemStack setUUID(ItemStack item, String key, UUID value) {
        return new NBTWrapper(item).setString(key, value.toString()).build();
    }

    public static UUID getUUID(ItemStack item, String key) {
        try {
            return UUID.fromString(NBTUtil.getString(item, key));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean hasKey(ItemStack item, String key) {
        return new NBTWrapper(item).hasKey(key);
    }

    public static ItemStack removeKey(ItemStack item, String key) {
        return new NBTWrapper(item).remove(key).build();
    }
}

