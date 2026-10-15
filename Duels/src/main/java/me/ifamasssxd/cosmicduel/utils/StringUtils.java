/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.utils;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StringUtils {
    public static String getFormattedName(ItemStack item) {
        return org.apache.commons.lang.StringUtils.capitaliseAllWords((String)item.getType().name().toLowerCase().replace("_", " "));
    }

    public static String getFormattedName(Material slot) {
        return org.apache.commons.lang.StringUtils.capitaliseAllWords((String)slot.name().toLowerCase().replace("_", " "));
    }

    public static List<String> trimLines(String line, ChatColor filler, int length) {
        ArrayList retr = Lists.newArrayList();
        if (line.length() < length || !line.contains(" ")) {
            retr.add(line);
            return retr;
        }
        StringBuilder stringLine = new StringBuilder();
        for (String word : line.split(" ")) {
            if (stringLine.length() + word.length() > length) {
                retr.add(stringLine.toString().trim());
                stringLine = new StringBuilder();
                stringLine.append((Object)filler).append(word).append(" ");
                continue;
            }
            stringLine.append(word).append(" ");
        }
        retr.add(stringLine.toString().trim());
        return retr;
    }
}

