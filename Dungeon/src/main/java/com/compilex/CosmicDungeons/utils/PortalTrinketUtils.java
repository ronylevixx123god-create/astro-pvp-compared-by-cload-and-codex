/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  org.bukkit.ChatColor
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.compilex.CosmicDungeons.utils;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.util.List;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalTrinketUtils {
    public static boolean portalHasTrinket(ItemStack item, String trinket) {
        if (!DungeonsAPI.get().isDungeonPortal(item)) {
            return false;
        }
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("cosmicData");
        return data.getString("dungeonTrinket").equals(trinket);
    }

    public static boolean portalHasTrinket(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("cosmicData");
        return data.hasKey("dungeonTrinket");
    }

    public static void applyTrinket(ItemStack item, String trinket, String displayName) {
        if (!DungeonsAPI.get().isDungeonPortal(item) || PortalTrinketUtils.portalHasTrinket(item, trinket)) {
            return;
        }
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("cosmicData");
        data.setString("dungeonTrinket", trinket);
        wrapper.setNBTBase("cosmicData", (NBTBase)data.get());
        item = wrapper.build();
        ItemMeta meta = item.getItemMeta();
        List lore = meta.getLore();
        lore.add(CC.GoldB + "TRINKET: " + CC.Gold + displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static int getTierFromName(String name) {
        String last = ChatColor.stripColor((String)name.split(" ")[name.split(" ").length - 1]);
        String sub = last.substring(1, last.length() - 1);
        return sub.equals("I") ? 1 : (sub.equals("II") ? 2 : 3);
    }
}

